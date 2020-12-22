package Relationelle;

import java.awt.*;
import java.awt.geom.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.mindfusion.diagramming.*;
import com.mindfusion.diagramming.LayeredLayout;
import com.mindfusion.diagramming.jlayout.Orientation;


/**
 * @description Main class for the sample application.
 *
 * @author MindFusion LLC
 * @version 1.0 $Date: 11/09/2016
 */

public class MainWindow extends JFrame {


    private Diagram diagram;
    private DiagramView diagramView;
    private JScrollPane _scrollPane;
    private ZoomControl zoomer;
    private  ArrayList<String> tables;

    public MainWindow() {
    	
    }
    public MainWindow(String log,String pw)
    {
        super("Modele Relationelle");
    	//System.out.println(log+"."+pw);

        setVisible(true);
       // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //initialize controls and table data
        initialize();

        //create DB tables
        createTables(log,pw);

        createRelationships();

        arrangeDiagram();

        setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);


    }

    private void initialize()
    {
        //diagram initialization
        diagram = new Diagram();
        diagram.setAutoResize(AutoResize.RightAndDown);

        //initialize a diagramView that will render the diagram.
        diagramView = new DiagramView(diagram);
        diagramView.setVisible(true);

        //provide a zoomer for the diagram
        zoomer = new ZoomControl();
        zoomer.setView(diagramView);
        zoomer.setPreferredSize(new Dimension(70, 50));
        zoomer.setVisible(true);

        //use a scroll pane to host large diagrams
        _scrollPane = new JScrollPane(diagramView);
        _scrollPane.setVisible(true);
        _scrollPane.setAutoscrolls(true);

        getContentPane().setLayout(new BorderLayout());
        this.add(zoomer, BorderLayout.EAST);
        this.add(_scrollPane, BorderLayout.CENTER);

        //diagram settings
        diagram.setTableColumnCount(4);
        diagram.setTableRowHeight(10f);
        diagram.setShadowsStyle(ShadowsStyle.None);
        diagram.setEnableStyledText(true);

        //read data for the tables
        tables = new ArrayList<String>();

        try
        {
            tables = DBMetaData.getTablesMetadata();
        }catch (SQLException e)
        {
            System.err.println("Error: " + e.getMessage());
        }
    }


    private void createTables(String log,String pw)
    {
        for( String tableName: tables)
        {
            try
            {
                ArrayList<DBColumn> tableData = DBMetaData.getColumnsMetadata(tableName,log,pw);

                Dimension tableSize = new Dimension(50, 30);
                TableNode _table = diagram.getFactory().createTableNode(10, 10, 50, tableData.size() * 8, 4, tableData.size());
                _table.setCaption("<b>" + tableName + "</b>");
                _table.setId(tableName);

                //customize the tables
                _table.setCaptionFormat(new TextFormat(Align.Center, Align.Center));
                _table.setCaptionHeight(7f);
                _table.setRowHeight(10f);
                _table.setAllowResizeColumns(true);
                _table.getColumns().get(0).setWidth(22f);
                _table.setShape(SimpleShape.RoundedRectangle);
                _table.setBrush(new SolidBrush(new Color((int)153, (int)179, (int)255)));

                int rowIndex = 0;

                for(DBColumn column: tableData)
                {
                    _table.getCell(1, rowIndex).setText("<b>" + column.name + "</b>");
                    _table.getCell(2, rowIndex).setText(column.type);
                    _table.getCell(3, rowIndex).setText(column.size);

                    //if the column is a primary key - set an image. If not, leave it empty.
                    if(column.isPrimaryKey())
                    {
                    }

                    rowIndex++;

                }

                //adjust the size of the tables and columns.
                _table.resizeToFitText(true);
                Rectangle2D.Float t_size = _table.getBounds();
                _table.getColumns().get(0).setWidth(7);
                _table.resize(t_size.width + 7, t_size.height);
                _table.resizeToFitImage();


            }catch(SQLException s)
            {
                System.err.println(s.getMessage());
            }
        }
    }

    private void createRelationships()
    {
        try
        {
            ArrayList<DBRelation> relations = DBMetaData.getRelationsMetadata(tables);

            for(DBRelation relation: relations)
            {
                TableNode source = (TableNode)diagram.findNodeById(relation.pk_table);
                TableNode destination = (TableNode)diagram.findNodeById(relation.fk_table);

                if(source != null && destination != null)
                {
                    int pk_index = -1;
                    int fk_index =  -1;

                    int rowCount = source.getRowCount();

                    for(int i = 0; i < rowCount; i++)
                    {
                        Cell cell = source.getCell(1, i);

                        if(cell.getText().equals("<b>" + relation.pk_key + "</b>"))
                        {
                            pk_index = i;
                            break;
                        }
                    }

                    rowCount = destination.getRows().size();

                    for(int i = 0; i < rowCount; i++) {
                        Cell cell = destination.getCell(1, i);
                        if(cell.getText().equals("<b>" + relation.fk_key + "</b>")) {
                            fk_index = i;
                            break;
                        }
                    }

                    DiagramLink link = diagram.getFactory().createDiagramLink(source, pk_index, destination, fk_index);
                    link.setBaseShape(ArrowHeads.RevWithLine);
                    link.setBaseShapeSize(3f);
                    link.setHeadShapeSize(3f);
                    link.setShape(LinkShape.Cascading);
                }
            }
        }catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
    }

    private void arrangeDiagram()
    {
        //use LayeredLayout with some initial customization
        LayeredLayout layout = new LayeredLayout(Orientation.Horizontal, 30f, 25f, 5f, 5f);
        layout.arrange(diagram);

        //adjust link position
        for (DiagramLink link: diagram.getLinks()) {

            if (link.getOrigin().getBounds().getX() < link.getDestination().getBounds().getX())
                link.setEndPoint(new Point2D.Float(link.getDestination().getBounds().x, link.getEndPoint().y));
        }
        //re-route all links
        diagram.setLinkRouter(new GridRouter());
        diagram.routeAllLinks();

        //resize the diagram after the layout to fit all items
        diagram.resizeToFitItems(20);
        diagramView.scrollTo(diagram.getBounds().width/2, 0);

        //customize the links
        diagram.setLinkCrossings(LinkCrossings.Arcs);
        diagram.setRoundedLinks(true);
        diagram.setRoundedLinksRadius(3);
        //redraw the control
        diagram.repaint();
    }
    /**
     * Run the application.
     *
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainWindow window = null;
                try {
                    window = new MainWindow();
                    window.setVisible(true);
                }
                catch (Exception exp) {
                    exp.printStackTrace();
                }
            }
        });
    }
}
