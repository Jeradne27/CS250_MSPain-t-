package paint.Menus;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Button;

import javafx.scene.canvas.Canvas;

import paint.Menus.StyleMenu;

import paint.Tools.Lines;
import paint.Tools.Pencil;
import paint.Tools.Rectangles;
import paint.Tools.Squares;
import paint.Tools.Circles;
import paint.Tools.Ellipses;
import paint.Tools.Eraser;
import paint.Tools.Triangles;
import paint.Tools.Diamonds;
import paint.Tools.Hearts;
import paint.Tools.Pentagons;
import paint.Tools.RightTriangles;
import paint.Tools.Text;
import paint.Tools.Drawing;

/**
 * ToolMenu class represents the tool menu for the application.
 * It manages the selection and activation of different drawing tools, including lines, pencils, and various shapes.
 * Provides a menu bar with options for selecting tools and updating the style menu accordingly.
 *
 * This class is responsible for handling the user interface and interactions related to tool selection and activation.
 */

// IMPROVEMENTS:
// - Add keyboard shortcuts for frequently used tools.
// - Add more options for different types of drawing tools.
// - Improve the layout of the tool menu for better user experience.
// - Add a preview of the drawing before it is placed on the canvas.

// ISSUES:
// - No keyboard shortcuts for tool selection.
// - When you draw on the canvas, you can't relocate the drawing
// - There is no preview of the drawing before it is placed on the canvas.

public class ToolMenu {

    // Calls to outside classes
    private Drawing<?> activeTool;
    private MenuBar toolBar;

    private Lines lines;
    private Pencil pencil;
    private Eraser eraser;
    private Rectangles rectangles;
    private Squares squares;
    private Circles circles;
    private Ellipses ellipses;
    private Triangles triangles;
    private Diamonds diamonds;
    private Hearts hearts;
    private Pentagons pentagons;
    private RightTriangles rightTriangles;
    private Text text;
    private StyleMenu styleMenu;


    /**
     * Creates a new tool menu.
     * @param lines The lines tool.
     * @param pencil The pencil tool.
     * @param eraser The eraser tool.
     * @param rectangles The rectangles tool.
     * @param squares The squares tool.
     * @param circles The circles tool.
     * @param ellipses The ellipses tool.
     * @param triangles The triangles tool.
     * @param diamonds The diamonds tool.
     * @param pentagons The pentagons tool.
     * @param rightTriangles The right triangles tool.
     * @param styleMenu The style menu.
     * @param canvas The canvas to draw on.
     */
    public ToolMenu(Lines lines, Pencil pencil, Eraser eraser, Rectangles rectangles, Squares squares, Circles circles, Ellipses ellipses, Triangles triangles, Diamonds diamonds, Hearts hearts, Pentagons pentagons, RightTriangles rightTriangles, Text text,StyleMenu styleMenu, Canvas canvas) {
        this.lines = lines;
        this.pencil = pencil;
        this.eraser = eraser;
        this.rectangles = rectangles;
        this.squares = squares;
        this.circles = circles;
        this.ellipses = ellipses;
        this.triangles = triangles;
        this.diamonds = diamonds;
        this.hearts = hearts;
        this.pentagons = pentagons;
        this.rightTriangles = rightTriangles;
        this.text = text;
        this.styleMenu = styleMenu;
        
        toolBar = new MenuBar();

        Menu drawMenu = new Menu("Draw");
        MenuItem lineItem = new MenuItem("Line");
        MenuItem pencilItem = new MenuItem("Pencil");
        MenuItem eraserItem = new MenuItem("Eraser");
        MenuItem textItem = new MenuItem("Text");

        drawMenu.getItems().add(lineItem);
        drawMenu.getItems().add(pencilItem);
        drawMenu.getItems().add(eraserItem);
        drawMenu.getItems().add(textItem);
        toolBar.getMenus().add(drawMenu);

        Menu shapeMenu = new Menu("Shapes");

        MenuItem rectangleItem = new MenuItem("Rectangle");
        MenuItem squareItem = new MenuItem("Square");
        MenuItem circleItem = new MenuItem("Circle");
        MenuItem ellipseItem = new MenuItem("Ellipse");
        MenuItem triangleItem = new MenuItem("Triangle");
        MenuItem diamondItem = new MenuItem("Diamond");
        MenuItem heartItem = new MenuItem("Heart");
        MenuItem pentagonItem = new MenuItem("Pentagon");
        MenuItem rightTriangleItem = new MenuItem("Right Triangle");

        shapeMenu.getItems().add(rectangleItem);
        shapeMenu.getItems().add(squareItem);
        shapeMenu.getItems().add(circleItem);
        shapeMenu.getItems().add(ellipseItem);
        shapeMenu.getItems().add(triangleItem);
        shapeMenu.getItems().add(diamondItem);
        shapeMenu.getItems().add(heartItem);
        shapeMenu.getItems().add(pentagonItem);
        shapeMenu.getItems().add(rightTriangleItem);

        toolBar.getMenus().add(shapeMenu);
        
        lineItem.setOnAction(event -> {
            styleMenu.setSelectedTool(lines);
            selectTool(lines, canvas);
        });

        pencilItem.setOnAction(event -> {
            styleMenu.setSelectedTool(pencil);
            selectTool(pencil, canvas);
        });
        
        eraserItem.setOnAction(event -> {
            styleMenu.setSelectedTool(eraser);
            selectTool(eraser, canvas);
        });

        rectangleItem.setOnAction(event -> {
            styleMenu.setSelectedTool(rectangles);
            selectTool(rectangles, canvas);
        });
        squareItem.setOnAction(event -> {
            styleMenu.setSelectedTool(squares);
            selectTool(squares, canvas);
        });
        circleItem.setOnAction(event -> {
            styleMenu.setSelectedTool(circles);
            selectTool(circles, canvas);
        });
        ellipseItem.setOnAction(event -> {
            styleMenu.setSelectedTool(ellipses);
            selectTool(ellipses, canvas);
        });
        triangleItem.setOnAction(event -> {
            styleMenu.setSelectedTool(triangles);
            selectTool(triangles, canvas);
        });
        diamondItem.setOnAction(event -> {
            styleMenu.setSelectedTool(diamonds);
            selectTool(diamonds, canvas);
        });
        heartItem.setOnAction(event -> {
            styleMenu.setSelectedTool(hearts);
            selectTool(hearts, canvas);
        });
        pentagonItem.setOnAction(event -> {
            styleMenu.setSelectedTool(pentagons);
            selectTool(pentagons, canvas);
        });
        rightTriangleItem.setOnAction(event -> {
            styleMenu.setSelectedTool(rightTriangles);
            selectTool(rightTriangles, canvas);
        });
        textItem.setOnAction(event -> {
            styleMenu.setSelectedTool(text);
            selectTool(text, canvas);
        });
    }
    
    /**
     * Selects the given tool and activates it on the canvas.
     * @param nextTool The tool to select.
     * @param canvas The canvas to activate the tool on.
     */
    private void selectTool(Drawing<?> nextTool, Canvas canvas) {
        if (activeTool != null) {
            // Deactivate the currently active tool before activating the new one.
            activeTool.deactivateTool();
        }
        // Activate the new tool on the canvas.
        activeTool = nextTool;
        activeTool.activateTool(canvas);
    }

    /**
     * Returns the tool menu bar.
     * @return The tool menu bar.
     */
    public MenuBar getToolMenu() {
        return toolBar;
    }
}
