package paint.Menus;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

import javafx.scene.canvas.Canvas;

import paint.Menus.StyleMenu;

import javafx.scene.layout.HBox;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
import paint.Tools.FourPointStars;
import paint.Tools.FivePointStars;
import paint.Tools.SixPointStars;
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
// - Added elements for star shapes
// - Added icons and hover labels for tools and edit items

// ISSUES:
// - Some of the icons aren't consistent

public class ToolMenu {
    private final Canvas canvas;

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
    private FourPointStars fourPointStars;
    private FivePointStars fivePointStars;
    private SixPointStars sixPointStars;
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
     * @param fourPointStars The four-point stars tool.
     * @param fivePointStars The five-point stars tool.
     * @param sixPointStars The six-point stars tool.
     * @param text The text tool.
     * @param selection The selection tool.
     * @param styleMenu The style menu.
     * @param canvas The canvas to draw on.
     * @param file The file tab associated with the tool menu.
     */
    public ToolMenu(Lines lines, Pencil pencil, Eraser eraser, Rectangles rectangles, Squares squares, Circles circles, Ellipses ellipses, Triangles triangles, Diamonds diamonds, Hearts hearts, Pentagons pentagons, RightTriangles rightTriangles, FourPointStars fourPointStars, FivePointStars fivePointStars, SixPointStars sixPointStars, Text text, Selection selection, StyleMenu styleMenu, Canvas canvas, FileTab file) {
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
        this.fourPointStars = fourPointStars;
        this.fivePointStars = fivePointStars;
        this.sixPointStars = sixPointStars;
        this.text = text;
        this.selection = selection;
        this.styleMenu = styleMenu;
        this.canvas = canvas;
        this.file = file;
        
        toolBar = new MenuBar();

        // Creates the buttons for eraser, undo, redo, and clear actions.
        Button eraserButton = new Button();
        eraserButton.setTooltip(new Tooltip("Eraser"));
        eraserButton.setGraphic(loadIcon("/Icons/eraser.png"));

        Button undoButton = new Button();
        undoButton.setTooltip(new Tooltip("Undo"));
        undoButton.setGraphic(loadIcon("/Icons/undo.png"));

        Button redoButton = new Button();
        redoButton.setTooltip(new Tooltip("Redo"));
        redoButton.setGraphic(loadIcon("/Icons/redo.png"));

        Button clearButton = new Button();
        clearButton.setTooltip(new Tooltip("Clear Canvas"));
        clearButton.setGraphic(loadIcon("/Icons/clear.png"));

        // Sets the action handlers for the buttons.
        undoButton.setOnAction(event -> file.undo());
        redoButton.setOnAction(event -> file.redo());
        clearButton.setOnAction(event -> file.clearCanvas());

        // Creates the horizontal box containing the tool bar and the action buttons.
        toolMenuRow = new HBox(5, toolBar, eraserButton, undoButton, redoButton, clearButton);
        toolMenuRow.getStyleClass().add("tool-menu-row");

        // Creates the draw menu and adds it to the tool bar.
        Menu drawMenu = new Menu("Draw");

        MenuItem lineItem = new MenuItem();
        lineItem.setGraphic(loadIcon("/Icons/line.png"));
        Tooltip.install(lineItem.getGraphic(), new Tooltip("Line"));

        MenuItem pencilItem = new MenuItem();
        pencilItem.setGraphic(loadIcon("/Icons/pencil.png"));
        Tooltip.install(pencilItem.getGraphic(), new Tooltip("Pencil"));

        MenuItem textItem = new MenuItem();
        textItem.setGraphic(loadIcon("/Icons/text.png"));
        Tooltip.install(textItem.getGraphic(), new Tooltip("Text"));

        MenuItem selectItem = new MenuItem();
        selectItem.setGraphic(loadIcon("/Icons/select.png"));
        Tooltip.install(selectItem.getGraphic(), new Tooltip("Select"));

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
        MenuItem copyItem = new MenuItem();
        copyItem.setGraphic(loadIcon("/Icons/copy.png"));
        Tooltip.install(copyItem.getGraphic(), new Tooltip("Copy"));

        MenuItem cutItem = new MenuItem();
        cutItem.setGraphic(loadIcon("/Icons/cut.png"));
        Tooltip.install(cutItem.getGraphic(), new Tooltip("Cut"));

        MenuItem pasteItem = new MenuItem();
        pasteItem.setGraphic(loadIcon("/Icons/paste.png"));
        Tooltip.install(pasteItem.getGraphic(), new Tooltip("Paste"));

        // Adds the menu items to the edit menu.
        editMenu.getItems().addAll(copyItem, cutItem, pasteItem);

        // Creates the menu items for the shapes menu.
        MenuItem rectangleItem = new MenuItem();
        rectangleItem.setGraphic(loadIcon("/Icons/rectangle.png"));
        Tooltip.install(rectangleItem.getGraphic(), new Tooltip("Rectangle"));

        MenuItem squareItem = new MenuItem();
        squareItem.setGraphic(loadIcon("/Icons/square.png"));
        Tooltip.install(squareItem.getGraphic(), new Tooltip("Square"));
        

        MenuItem circleItem = new MenuItem();
        circleItem.setGraphic(loadIcon("/Icons/circle.png"));
        Tooltip.install(circleItem.getGraphic(), new Tooltip("Circle"));
        
        MenuItem ellipseItem = new MenuItem();
        ellipseItem.setGraphic(loadIcon("/Icons/ellipse.png"));
        Tooltip.install(ellipseItem.getGraphic(), new Tooltip("Ellipse"));
        
        MenuItem triangleItem = new MenuItem();
        triangleItem.setGraphic(loadIcon("/Icons/triangle.png"));
        Tooltip.install(triangleItem.getGraphic(), new Tooltip("Triangle"));
        
        MenuItem diamondItem = new MenuItem();
        diamondItem.setGraphic(loadIcon("/Icons/diamond.png"));
        Tooltip.install(diamondItem.getGraphic(), new Tooltip("Diamond"));
        
        MenuItem heartItem = new MenuItem();
        heartItem.setGraphic(loadIcon("/Icons/heart.png"));
        Tooltip.install(heartItem.getGraphic(), new Tooltip("Heart"));
        
        MenuItem pentagonItem = new MenuItem();
        pentagonItem.setGraphic(loadIcon("/Icons/pentagon.png"));
        Tooltip.install(pentagonItem.getGraphic(), new Tooltip("Pentagon"));

        MenuItem rightTriangleItem = new MenuItem();
        rightTriangleItem.setGraphic(loadIcon("/Icons/rightTriangle.png"));
        Tooltip.install(rightTriangleItem.getGraphic(), new Tooltip("Right Triangle"));

        MenuItem fourPointStarItem = new MenuItem();
        fourPointStarItem.setGraphic(loadIcon("/Icons/fourPointStar.png"));
        Tooltip.install(fourPointStarItem.getGraphic(), new Tooltip("Four-Point Star"));

        MenuItem fivePointStarItem = new MenuItem();
        fivePointStarItem.setGraphic(loadIcon("/Icons/fivePointStar.png"));
        Tooltip.install(fivePointStarItem.getGraphic(), new Tooltip("Five-Point Star"));

        MenuItem sixPointStarItem = new MenuItem();
        sixPointStarItem.setGraphic(loadIcon("/Icons/sixPointStar.png"));
        Tooltip.install(sixPointStarItem.getGraphic(), new Tooltip("Six-Point Star"));
        
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
        shapeMenu.getItems().add(fourPointStarItem);
        shapeMenu.getItems().add(fivePointStarItem);
        shapeMenu.getItems().add(sixPointStarItem);

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

        sixPointStarItem.setOnAction(event -> {
            styleMenu.setSelectedTool(sixPointStars);
            selectTool(sixPointStars, canvas);
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

        fourPointStarItem.setOnAction(event -> {
            styleMenu.setSelectedTool(fourPointStars);
            selectTool(fourPointStars, canvas);
        });

        fivePointStarItem.setOnAction(event -> {
            styleMenu.setSelectedTool(fivePointStars);
            selectTool(fivePointStars, canvas);
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
     * Loads an icon from the specified path.
     * @param path The path to the icon file.
     * @return The loaded ImageView representing the icon.
     */
    private ImageView loadIcon(String path) {
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(path)));

        icon.setFitWidth(16);
        icon.setFitHeight(16);
        icon.setPreserveRatio(true);

        return icon;
    }

    /**
     * Returns the tool menu bar.
     * @return The tool menu bar.
     */
    public HBox getToolMenu() {
        return toolMenuRow;
    }
}
