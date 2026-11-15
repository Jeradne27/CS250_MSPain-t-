package paint.Menus;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Button;

import javafx.scene.canvas.Canvas;

import paint.Menus.StyleMenu;

import javafx.scene.layout.HBox;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import paint.Operations.FileTab;

import paint.Tools.Text;
import paint.Tools.Lines;
import paint.Tools.Pencil;
import paint.Tools.Eraser;
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
import paint.Tools.Drawing;
import paint.Tools.Selection;

/**
 * ToolMenu class represents the tool menu for the application.
 * It manages the selection and activation of different drawing tools, including lines, pencils, and various shapes.
 * Provides a menu bar with options for selecting tools and updating the style menu accordingly.
 *
 * This class is responsible for handling the user interface and interactions related to tool selection and activation.
 */

// IMPROVEMENTS:
// - Added Hearts, Pentagons, Text, and Right Triangles tools.
// - Added copy, paste, and cut under a new edit menu.
// - Moved eraser, undo, redo, and clear to into the menubar in order to keep things separated

// ISSUES:
// - No keyboard shortcuts for tool selection.
// - Move the eraser, undo, redo, and clear options somewhere else

public class ToolMenu {
    private final Canvas canvas;

    // Calls to outside classes
    private Drawing<?> activeTool;
    private MenuBar toolBar;

    private HBox toolMenuRow;

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
    private Selection selection;
    private StyleMenu styleMenu;
    private FileTab file;


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
     * @param hearts The hearts tool.
     * @param pentagons The pentagons tool.
     * @param rightTriangles The right triangles tool.
     * @param text The text tool.
     * @param selection The selection tool.
     * @param styleMenu The style menu.
     * @param canvas The canvas to draw on.
     * @param file The file tab associated with the tool menu.
     */
    public ToolMenu(Lines lines, Pencil pencil, Eraser eraser, Rectangles rectangles, Squares squares, Circles circles, Ellipses ellipses, Triangles triangles, Diamonds diamonds, Hearts hearts, Pentagons pentagons, RightTriangles rightTriangles, Text text, Selection selection, StyleMenu styleMenu, Canvas canvas, FileTab file) {
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
        this.selection = selection;
        this.styleMenu = styleMenu;
        this.canvas = canvas;
        this.file = file;
        
        toolBar = new MenuBar();

        // Creates the buttons for eraser, undo, redo, and clear actions.
        Button eraserButton = new Button("Eraser");
        Button undoButton = new Button("Undo");
        Button redoButton = new Button("Redo");
        Button clearButton = new Button("Clear");

        // Sets the action handlers for the buttons.
        undoButton.setOnAction(event -> file.undo());
        redoButton.setOnAction(event -> file.redo());
        clearButton.setOnAction(event -> file.clearCanvas());

        // Creates the horizontal box containing the tool bar and the action buttons.
        toolMenuRow = new HBox(5, toolBar, eraserButton, undoButton, redoButton, clearButton);
        toolMenuRow.getStyleClass().add("tool-menu-row");

        // Creates the draw menu and adds it to the tool bar.
        Menu drawMenu = new Menu("Draw");
        MenuItem lineItem = new MenuItem("Line");
        MenuItem pencilItem = new MenuItem("Pencil");
        MenuItem textItem = new MenuItem("Text");
        MenuItem selectItem = new MenuItem("Select");
        drawMenu.getItems().add(lineItem);
        drawMenu.getItems().add(pencilItem);
        drawMenu.getItems().add(textItem);
        drawMenu.getItems().add(selectItem);
        toolBar.getMenus().add(drawMenu);

        // Creates the shapes menu and adds it to the tool bar.
        Menu shapeMenu = new Menu("Shapes");

        // Creates the edit menu and adds it to the tool bar.
        Menu editMenu = new Menu("Edit");

        // Creates the menu items for the edit menu.
        MenuItem copyItem = new MenuItem("Copy");
        MenuItem cutItem = new MenuItem("Cut");
        MenuItem pasteItem = new MenuItem("Paste");

        // Adds the menu items to the edit menu.
        editMenu.getItems().addAll(copyItem, cutItem, pasteItem);

        // Creates the menu items for the shapes menu.
        MenuItem rectangleItem = new MenuItem("Rectangle");
        MenuItem squareItem = new MenuItem("Square");
        MenuItem circleItem = new MenuItem("Circle");
        MenuItem ellipseItem = new MenuItem("Ellipse");
        MenuItem triangleItem = new MenuItem("Triangle");
        MenuItem diamondItem = new MenuItem("Diamond");
        MenuItem heartItem = new MenuItem("Heart");
        MenuItem pentagonItem = new MenuItem("Pentagon");
        MenuItem rightTriangleItem = new MenuItem("Right Triangle");

        // Adds the menu items to the shapes menu.
        shapeMenu.getItems().add(rectangleItem);
        shapeMenu.getItems().add(squareItem);
        shapeMenu.getItems().add(circleItem);
        shapeMenu.getItems().add(ellipseItem);
        shapeMenu.getItems().add(triangleItem);
        shapeMenu.getItems().add(diamondItem);
        shapeMenu.getItems().add(heartItem);
        shapeMenu.getItems().add(pentagonItem);
        shapeMenu.getItems().add(rightTriangleItem);

        // Adds the shapes and edit menus to the tool bar.
        toolBar.getMenus().addAll(shapeMenu, editMenu);

        // Sets the action handlers for the draw menu items.
        lineItem.setOnAction(event -> {
            styleMenu.setSelectedTool(lines);
            selectTool(lines, canvas);
        });

        pencilItem.setOnAction(event -> {
            styleMenu.setSelectedTool(pencil);
            selectTool(pencil, canvas);
        });

        textItem.setOnAction(event -> {
            styleMenu.setSelectedTool(text);
            selectTool(text, canvas);
        });

        selectItem.setOnAction(event -> {
            styleMenu.setSelectedTool(selection);
            selectTool(selection, canvas);
        });

        // Sets the action handlers for the shapes menu items.
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

        // Sets the action handler for the eraser button.
        eraserButton.setOnAction(event -> {
            styleMenu.setSelectedTool(eraser);
            selectTool(eraser, canvas);
        });

        // Sets the action handlers for the copy, cut, and paste menu items.
        copyItem.setOnAction(event -> selection.copySelection());
        copyItem.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));

        cutItem.setOnAction(event -> selection.cutSelection());
        cutItem.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));

        pasteItem.setOnAction(event -> {
            selectTool(selection, canvas);
            selection.pasteSelection();
        });
        pasteItem.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN));
    }

    /**
     * Creates a menu item for the given tool.
     * @param label The label of the menu item.
     * @param tool The tool associated with the menu item.
     * @return The created menu item.
     */
    private MenuItem toolItem(String label, Drawing<?> tool) {
        MenuItem item = new MenuItem(label);
        item.setOnAction(event -> selectTool(tool, canvas));
        return item;
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
        styleMenu.setSelectedTool(activeTool);
        activeTool.activateTool(canvas);
    }

    /**
     * Returns the tool menu bar.
     * @return The tool menu bar.
     */
    public HBox getToolMenu() {
        return toolMenuRow;
    }
}
