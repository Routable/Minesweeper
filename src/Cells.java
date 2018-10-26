
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;



public class Cells extends Button  {
	


ImageView cover = new ImageView(new Image("res/cover.png"));					//default image for cells when game loads


	
	
int status; boolean hasBomb; int totalEmptyTiles; int x; int y; boolean isFlagged; boolean isClicked;
	

	public Cells(Boolean hasBomb, int status, int x, int y) {
		this.hasBomb = hasBomb; 
		this.status = status; 
		this.x = x; 
		this.y = y; 
		double size = 32; 
		cover.setFitWidth(size); //make cell button images fit fully in cell
		cover.setFitHeight(size); //make cell button images fit fully in cell
		setGraphic(cover);        //set graphic images for buttons. Cover is the default image.
		setPadding(Insets.EMPTY); //eliminates spacing in-between cell images	
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getStatus() {
		return status;
	} 
	
	public void isClicked() {
		isClicked = true;
	}
	
	public boolean getClicked() {
		return isClicked;
	}

} 


	


