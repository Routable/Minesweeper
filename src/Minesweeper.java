import java.util.Random;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Minesweeper extends Application {

	public final int beginner = 8; 
	public final int intermediate = 16; 
	public final int hard = 32; 

	public int size_width = 8; //size of board width - change this to affect sizing for entire board. DO NOT TOUCHING ANYTHING ELSE!
	public int size_height = 8; //size of board height - change this to affect sizing for entire board. DO NOT TOUCH ANYTTHING ELSE!
	public int difficulty = 10; //how many mines to populate the board with - DO NOT CHANGE ANYTHING ELSE!
	int[][] minefield;
	public int totalEmptyTiles; boolean hasLost = false; Button smileybutton; HBox pane; int minesPlaced; Label rightTimer; Label leftBombsLeft; int totalClicks; int button; 

	ImageView face = new ImageView("res/face-smile.png");
	ImageView facewin = new ImageView("res/face-win.png");
	ImageView facedead = new ImageView("res/face-dead.png");
	ImageView zero = new ImageView(new Image("res/0.png"));  //empty space
	ImageView one = new ImageView(new Image("res/1.png"));   //1 mine touching
	ImageView two = new ImageView(new Image("res/2.png"));   //2 mine touching
	ImageView three = new ImageView(new Image("res/3.png")); //3 mine touching
	ImageView four = new ImageView(new Image("res/4.png"));  //4 mine touching
	ImageView five = new ImageView(new Image("res/5.png"));  //5 mine touching
	ImageView six = new ImageView(new Image("res/6.png"));   //6 mine touching
	ImageView seven = new ImageView(new Image("res/7.png")); //7 mine touching
	ImageView eight = new ImageView(new Image("res/8.png")); //8 mine touching
	ImageView revealbomb = new ImageView(new Image("res/revealbomb.png"));			//after win/lose, image for bombs that are left
	ImageView cover = new ImageView(new Image("res/cover.png"));					//default image for cells when game loads
	ImageView minegrey = new ImageView(new Image("res/mine-grey.png"));				//a grey mine image
	ImageView minered = new ImageView(new Image("res/mine-red.png"));				//when user clicks cell with mine
	ImageView minemisflagged = new ImageView(new Image("res/mine-misflagged.png"));	//misflagged mine image
	ImageView flag = new ImageView(new Image("res/flag.png"));


	//START STAGE ----------------------------------------------------------------------------------------------------------------------

	public void start(Stage theStage) {


		BorderPane bp = new BorderPane(); 
		pane = new HBox(20);
		pane.setPadding(new Insets(0, 0, 0, 0)); 
		pane.setAlignment(Pos.CENTER); 
		GridPane gp = new GridPane();
		bp.setCenter(pane);  //header stuffs
		bp.setBottom(gp); //body for buttons n shit


		VBox vb = new VBox();
		vb.setPadding(new Insets(0, 0, 0, 0));
		bp.setTop(vb);

		MenuBar menuBar = new MenuBar();
		Menu dmenu = new Menu("Options");
		menuBar.getMenus().add(dmenu);
		vb.getChildren().add(menuBar);
		MenuItem bmenu = new MenuItem("Beginner");
		MenuItem imenu = new MenuItem("Intermediate");
		MenuItem hmenu = new MenuItem("Hard");
		MenuItem qmenu = new MenuItem("Quit");
		dmenu.getItems().addAll(bmenu, imenu, hmenu, qmenu);

		leftBombsLeft = new Label("Start");
		rightTimer = new Label("End");

		bmenu.setOnAction( e -> {
			size_width = beginner;
			size_height = beginner;
			difficulty = 10;
			minefield = new int[size_width][size_height]; 
			start(theStage);
		});
		imenu.setOnAction( e -> {
			size_width = intermediate;
			size_height = intermediate;
			difficulty = 40;
			minefield = new int[size_width][size_height]; 
			start(theStage);
		});
		hmenu.setOnAction( e -> {
			size_width = hard;
			size_height = intermediate;
			difficulty = 99; 
			minefield = new int[size_width][size_height]; 
			start(theStage);
		});
		qmenu.setOnAction( e -> {
			System.exit(0);
		});


		smileybutton = new Button(); 
		smileybutton.setGraphic(face); 

		smileybutton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				System.out.println("Restart");  
				start(theStage);

			}
		});

		pane.getChildren().add(leftBombsLeft); 
		pane.getChildren().add(smileybutton);
		pane.getChildren().add(rightTimer); 


		Cells cell[][] = new Cells[size_width][size_height];
		placeMine();
		setCellValues(minefield);

		//CELL POPULATION --------------------------------------------------------------------------------------------------------


		for(int i = 0; i < size_width; i++) {   //changed at 730pm
			for(int j = 0; j < size_height; j++) {

				if(getValue(i, j) == -1) {
					cell[i][j] = new Cells(true, getValue(i,j), i, j);} //setting hasBomb to true because bomb is here
				else{
					cell[i][j] = new Cells(false, getValue(i,j), i, j);} //no bomb here, no terrorist threats 
				gp.add(cell[i][j], i, j);

				Cells c = cell[i][j];

				c.setOnMouseClicked( e-> {
					MouseButton button = e.getButton();

					if(button == MouseButton.PRIMARY && !c.isFlagged && !hasLost) {
						
				
						if(c.hasBomb) {
							hasLost = true; 
							smileybutton.setGraphic(new ImageView("res/face-dead.png")); 
							for(int a = 0; a < size_width; a++) {
								for(int b = 0; b < size_height; b++) {
									if(cell[a][b].hasBomb) {
										cell[a][b].setGraphic(new ImageView(new Image("res/mine-grey.png")));

									}

								}
							}
							c.setGraphic(new ImageView(new Image("res/mine-red.png")));

						}

						else {

							totalEmptyTiles--;
							if(totalEmptyTiles == 0) {
								smileybutton.setGraphic(new ImageView("res/face-win.png"));
							}
								c.setGraphic(new ImageView(new Image("res/" + c.getStatus() + ".png")));
								c.isClicked();
						}
						totalClicks++;
						System.out.println(totalClicks);
					}

					if (button == MouseButton.SECONDARY && !hasLost) {
						if(c.isFlagged) {
							c.isFlagged = false;
							minesPlaced++;
							leftBombsLeft.textProperty().setValue(Integer.toString(minesPlaced));
							c.setGraphic(cover);

						}
						else if(!c.getClicked()) {
							c.setGraphic(new ImageView(new Image("res/flag.png")));
							c.isFlagged = true;
							minesPlaced--;
							leftBombsLeft.textProperty().setValue(Integer.toString(minesPlaced));
						}

					}

				});
			}
		}


		//END CELLS -------------------------------------------------------------------------------------------------------------------------

		Scene scene = new Scene(bp);
		theStage.setTitle("Minesweeper");
		theStage.setScene(scene);
		theStage.show();

		//END STAGE ----------------------------------------------------------------------------------------------------------------------
	}


	public void placeMine() {

		try {

			hasLost = false;  
			minesPlaced = 0;
			minefield = new int[size_width][size_height]; 
			totalEmptyTiles = size_width * size_height - difficulty;


			for(int i = 0; i < difficulty; i++) {
				boolean mineAlreadyPlaced;

				do {

					int rndx = new Random().nextInt(size_width); //randomly generates X coordinate value for mine placement

					int rndy = new Random().nextInt(size_height); //randomly generates Y coordinate value for mine placement


					if(minefield[rndx][rndy] == 0) { //loop goes through array - if spot has not already been marked as a mine, we will mark that spot as a mine.
						minefield[rndx][rndy] = -1; 
						mineAlreadyPlaced = false;
						minesPlaced++;
						leftBombsLeft.textProperty().setValue(Integer.toString(minesPlaced));
						System.out.println(minesPlaced);
					} 
					else {
						mineAlreadyPlaced = true;
					}
				}while(mineAlreadyPlaced);

			}
		}catch(ArrayIndexOutOfBoundsException ex) {
			System.out.println(ex);
			System.out.println("God damnit!"); 
		}

	}


	public int getValue(int x, int y) {
		try {

			return minefield[x][y];
		}catch(ArrayIndexOutOfBoundsException ey) {

			System.out.println("Exception. God damnit!");
			return 0;
		}
	}

	public void setCellValues(int[][] minefield2) throws ArrayIndexOutOfBoundsException {

		try {
			for(int x = 0; x < size_width; x++) {
				for(int y = 0; y < size_height; y++) {

					//System.out.println(x + " " + y);
					if(minefield[x][y] != -1 && isFilledAt(x,y)) { //not a mine
						//System.out.println("After IF");
						//System.out.println("Not a mine!");
						if(isFilledAt(x-1, y) && getValue(x-1, y) == -1) minefield[x][y] += 1; 
						if(isFilledAt(x+1, y) && getValue(x+1, y) == -1) minefield[x][y] += 1; 
						if(isFilledAt(x+1, y-1) && getValue(x+1, y-1) == -1) minefield[x][y] += 1; 
						if(isFilledAt(x-1, y-1) && getValue(x-1, y-1) == -1) minefield[x][y] += 1; 
						if(isFilledAt(x, y-1) && getValue(x, y-1) == -1) minefield[x][y] += 1; 
						if(isFilledAt(x-1, y+1) && getValue(x-1, y+1) == -1) minefield[x][y] += 1; 
						if(isFilledAt(x, y+1) && getValue(x, y+1) == -1) minefield[x][y] += 1; 
						if(isFilledAt(x+1, y+1) && getValue(x+1, y+1) == -1) minefield[x][y] += 1; 	
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();}
	} 

	public boolean isFilledAt(int width, int height)  { //CAUSE IM SICK OF THESE OUT OF ARRAY ERRORS. NEVER GOING TO DEAL WITH THIS AGAIN!
		try {	

			//System.out.println("My height is: " + size_height + " and my width is: " + size_width);
			//System.out.println("is filled at: " + row + " " + col);
			boolean toReturn = false;
			if (width < 0 || width >= size_height || height < 0 || height >= size_width) {
				toReturn = false;
				throw new Exception("Out of Bounds!");
			} else {
				toReturn = true;
			}
			return toReturn;
		} catch(Exception ex) {
			//System.out.println(ex);
			return false; }}	

	
	public void revealOpenCells(int[][] minefield2, int x, int y) throws ArrayIndexOutOfBoundsException {

		
		try {

			
			
			if(isFilledAt(x-1, y) && getValue(x-1, y) > 0) {
				minefield[x-1][y] = 20;
				revealOpenCells(minefield, x-1, y);
			}else if(isFilledAt(x+1, y) && getValue(x+1, y) > 0) {
				minefield[x+1][y] = 20;
				revealOpenCells(minefield, x+1, y);
			}else if(isFilledAt(x+1, y-1) && getValue(x+1, y-1) > 0) {
				minefield[x+1][y-1] = 20;
				revealOpenCells(minefield, x+1, y-1);
				
			}else if(isFilledAt(x-1, y-1) && getValue(x-1, y-1) > 0) {
				minefield[x-1][y-1] = 20;
				revealOpenCells(minefield, x-1, y-1);
			}else if(isFilledAt(x, y-1) && getValue(x, y-1) > 0) {
				minefield[x][y-1] = 20;
				revealOpenCells(minefield, x, y-1);
			}else if(isFilledAt(x-1, y+1) && getValue(x-1, y+1) > 0) {
				minefield[x-1][y+1] = 20;
				revealOpenCells(minefield, x-1, y+1);
			}else if(isFilledAt(x, y+1) && getValue(x, y+1) > 0) {
				minefield[x][y+1] = 20;
				revealOpenCells(minefield, x, y+1);
			}else if(isFilledAt(x+1, y+1) && getValue(x+1, y+1) > 0) {
				minefield[x][y+1] = 20;
				revealOpenCells(minefield, x, y+1);
			}
			
	
		} catch (Exception e) {
			e.printStackTrace();}
	} 
} 





















