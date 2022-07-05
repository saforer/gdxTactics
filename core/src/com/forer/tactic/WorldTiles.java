package com.forer.tactic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class WorldTiles {
    int width = 5; //Default 5
    int height = 2; //Default 2
    Tile[][] tileArray;
    boolean selecting = false;
    boolean moving = false;
    float movingTimer = 0f;
    boolean facing = false;
    Coordinates selectorCoords = new Coordinates(0,0);
    Texture selectorTexture = new Texture("selector.png");
    Texture selectingTexture = new Texture("selected.png");
    Texture facingTexture;
    static Texture NEFacing, NWFacing, SEFacing, SWFacing;
    List<Character> characterList = new ArrayList<>();
    Character currentSelected;
    List<String> moveList;
    int moveCount = 0;
    int moveMax = 3;

    public WorldTiles() {
        fillTiles();
        NEFacing = new Texture("NEFacing.png");
        NWFacing = new Texture("NWFacing.png");
        SWFacing = new Texture("SWFacing.png");
        SEFacing = new Texture("SEFacing.png");
        facingTexture = NEFacing;
    }

    public WorldTiles (int width, int height) {
        this.width = width;
        this.height = height;
        fillTiles();
        NEFacing = new Texture("NEFacing.png");
        NWFacing = new Texture("NWFacing.png");
        SWFacing = new Texture("SWFacing.png");
        SEFacing = new Texture("SEFacing.png");
        facingTexture = NEFacing;
    }

    void fillTiles() {
        tileArray = new Tile[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tileArray[x][y] = new Tile();
            }
        }

        characterList.add(new Character(0,0));
        characterList.add(new Character(0,2));
        characterList.add(new Character(0,4));
        characterList.add(new Character(0,6));

    }

    public void update() {
        if (!facing) {

            if (!moving) {
                if (!selecting) {
                    //Cursor
                    if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                        Coordinates moveTo = Coordinates.moveUp(selectorCoords);
                        selectorCoords = moveTo;
                        updateSelectedTile();
                    } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                        Coordinates moveTo = Coordinates.moveDown(selectorCoords);
                        selectorCoords = moveTo;
                        updateSelectedTile();
                    } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                        Coordinates moveTo = Coordinates.moveLeft(selectorCoords);
                        selectorCoords = moveTo;
                        updateSelectedTile();
                    } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                        Coordinates moveTo = Coordinates.moveRight(selectorCoords);
                        selectorCoords = moveTo;
                        updateSelectedTile();
                    } else if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
                        if (doesTileExistThere(selectorCoords)) {
                            //Find character that was selected
                            currentSelected = null;
                            for (Character c:
                                 characterList) {
                                if ((c.pos.x == selectorCoords.x) && (c.pos.y == selectorCoords.y)) {
                                    currentSelected = c;
                                    selecting = true;
                                    moveList = new ArrayList<String>();
                                    moveMax = 3;
                                    moveCount = 0;
                                }
                            }
                        }

                    }
                } else {
                    //Selecting character Time
                    if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
                        if ((selectorCoords.x == currentSelected.pos.x) && (selectorCoords.y == currentSelected.pos.y)) {
                            selecting = false;
                            System.out.println("Movement canceled");
                        } else {
                            if (!doesCharacterExistThere(selectorCoords)) {
                                //Complete Move
                                for (String s :
                                        moveList) {
                                    System.out.println(s + "");
                                }
                                selecting = false;
                                moving = true;
                                movingTimer = 1f;
                            } else {
                                System.out.println("Cannot move there, Currently occupied");
                            }
                        }
                    } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                        if (doesTileExistThere(selectorCoords))
                            if (moveCount < moveMax) {
                                if (doesTileExistThere(Coordinates.moveUp(selectorCoords))) {
                                    if (!doesCharacterExistThere(Coordinates.moveUp(selectorCoords), currentSelected)) {
                                        Coordinates moveTo = Coordinates.moveUp(selectorCoords);
                                        selectorCoords = moveTo;
                                        updateSelectedTile();

                                        if (didMoveOpposite("Up")) {
                                            System.out.println("Undoing your Down move");
                                            int numberOfMoveToRemove = 0;
                                            for (int i = 0; i < moveList.size()-1; i++) {
                                                if (moveList.get(i).equals("Down")) {
                                                    numberOfMoveToRemove = i;
                                                }
                                            }
                                            moveList.remove(numberOfMoveToRemove);
                                            moveCount--;
                                            listMoveQueue();
                                        } else {
                                            moveList.add("Up");
                                            moveCount++;
                                        }
                                    } else {
                                        System.out.println("Cannot move there, Currently occupied");
                                    }
                                } else {
                                    System.out.println("Cannot move there, No tile there");
                                }
                            }
                    } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                        if (moveCount < moveMax) {
                            if (doesTileExistThere(Coordinates.moveDown(selectorCoords))) {
                                if (!doesCharacterExistThere(Coordinates.moveDown(selectorCoords), currentSelected)) {
                                    Coordinates moveTo = Coordinates.moveDown(selectorCoords);
                                    selectorCoords = moveTo;

                                    updateSelectedTile();

                                    if (didMoveOpposite("Down")) {
                                        System.out.println("Undoing your Up move");
                                        int numberOfMoveToRemove = 0;
                                        for (int i = 0; i < moveList.size()-1; i++) {
                                            if (moveList.get(i).equals("Up")) {
                                                numberOfMoveToRemove = i;
                                            }
                                        }
                                        moveList.remove(numberOfMoveToRemove);
                                        moveCount--;
                                        listMoveQueue();
                                    } else {
                                        moveList.add("Down");
                                        moveCount++;
                                    }
                                } else {
                                    System.out.println("Cannot move there, Currently occupied");
                                }
                            } else {
                                System.out.println("Cannot move there, No tile there");
                            }
                        }
                    } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                        if (moveCount < moveMax) {
                            if (doesTileExistThere(Coordinates.moveLeft(selectorCoords))) {
                                if (!doesCharacterExistThere(Coordinates.moveLeft(selectorCoords), currentSelected)) {
                                    Coordinates moveTo = Coordinates.moveLeft(selectorCoords);
                                    selectorCoords = moveTo;

                                    updateSelectedTile();

                                    if (didMoveOpposite("Left")) {
                                        System.out.println("Undoing your Right move");
                                        int numberOfMoveToRemove = 0;
                                        for (int i = 0; i < moveList.size()-1; i++) {
                                            if (moveList.get(i).equals("Right")) {
                                                numberOfMoveToRemove = i;
                                            }
                                        }
                                        moveList.remove(numberOfMoveToRemove);
                                        moveCount--;
                                        listMoveQueue();
                                    } else {
                                        moveList.add("Left");
                                        moveCount++;
                                    }
                                } else {
                                    System.out.println("Cannot move there, Currently occupied");
                                }
                            } else {
                                System.out.println("Cannot move there, No tile there");
                            }
                        }
                    } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                        if (moveCount < moveMax) {
                            if (doesTileExistThere(Coordinates.moveRight(selectorCoords))) {
                                if (!doesCharacterExistThere(Coordinates.moveRight(selectorCoords), currentSelected)) {
                                    Coordinates moveTo = Coordinates.moveRight(selectorCoords);
                                    selectorCoords = moveTo;

                                    updateSelectedTile();

                                    if (didMoveOpposite("Right")) {
                                        System.out.println("Undoing your left move");
                                        int numberOfMoveToRemove = 0;
                                        for (int i = 0; i < moveList.size()-1; i++) {
                                            if (moveList.get(i).equals("Left")) {
                                                numberOfMoveToRemove = i;
                                            }
                                        }
                                        moveList.remove(numberOfMoveToRemove);
                                        moveCount--;
                                        listMoveQueue();
                                    } else {
                                        moveList.add("Right");
                                        moveCount++;
                                    }
                                } else {
                                    System.out.println("Cannot move there, Currently occupied");
                                }
                            } else {
                                System.out.println("Cannot move there, No tile there");
                            }
                        }
                    }
                }
            } else {
                //Moving
                if (moveList.size() > 0) {
                    if (movingTimer <= 0) {
                        //Move the character
                        Coordinates moveFrom = currentSelected.pos;
                        String moveToDo = moveList.get(0);
                        moveList.remove(0);
                        Coordinates moveTo;
                        switch (moveToDo) {
                            case "Up":
                            default:
                                moveTo = Coordinates.moveUp(moveFrom);
                                currentSelected.facingUpdate("NE");
                                break;
                            case "Down":
                                moveTo = Coordinates.moveDown(moveFrom);
                                currentSelected.facingUpdate("SW");
                                break;
                            case "Left":
                                moveTo = Coordinates.moveLeft(moveFrom);
                                currentSelected.facingUpdate("NW");
                                break;
                            case "Right":
                                moveTo = Coordinates.moveRight(moveFrom);
                                currentSelected.facingUpdate("SE");
                                break;
                        }
                        currentSelected.pos = moveTo;
                        movingTimer = 1f;
                        updateCharacter();
                    } else {
                        movingTimer -= Gdx.graphics.getDeltaTime();
                        //Wait to move the character
                    }
                } else {
                    //List is empty, Time to set FACING and then go back to normal gameplay
                    facing = true;
                    moving = false;

                    //Draw facing indicator based on currently facing direction
                    switch (currentSelected.facing) {
                        default:
                        case "NE":
                            facingTexture = NEFacing;
                            break;
                        case "NW":
                            facingTexture = NWFacing;
                            break;
                        case "SE":
                            facingTexture = SEFacing;
                            break;
                        case "SW":
                            facingTexture = SWFacing;
                            break;
                    }
                }
            }
        } else {
            //FACING!
            if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
                facing = false;
                //FACING OVER time to go back to normal!
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                facingTexture = NEFacing;
                currentSelected.facingUpdate("NE");
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                facingTexture = SWFacing;
                currentSelected.facingUpdate("SW");
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                facingTexture = NWFacing;
                currentSelected.facingUpdate("NW");
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                facingTexture = SEFacing;
                currentSelected.facingUpdate("SE");
            }
        }
    }

    void listMoveQueue() {
        for (String s:
             moveList) {
            System.out.println("" + s);
        }
    }



    void updateSelectedTile() {
        System.out.println("Current Tile is " + selectorCoords.x + " " + selectorCoords.y);
    }

    void updateCharacter() {
        for (Character c:
             characterList) {
            System.out.println("Character tile is " + c.pos.x + " " + c.pos.y);
        }
    }

    String opposites (String in) {
        switch (in) {
            case "NW":
            default:
                return "SE";
            case "Up":
                return "Down";
            case "NE":
                return "SW";
            case "Right":
                return "Left";
            case "SE":
                return "NW";
            case "Down":
                return "Up";
            case "SW":
                return "NE";
            case "Left":
                return "Right";
        }
    }

    boolean didMoveOpposite (String move) {
        for (String s:
             moveList) {
            if (opposites(move).equals(s)) {
                //You have an undo move in here
                return true;
            }
        }
        return false;
    }

    void draw (SpriteBatch sb) {

        //Draw tile grid
        for (int y = height-1; y >= 0; y--) {
            for (int x = width-1; x >= 0; x--) {
                int tempOffset = (y % 2 != 0) ? 32 : 0;
                Tile t = tileArray[x][y];
                if (t.enabled) {
                    sb.draw(t.img, (64 * x) + tempOffset, (16 * y) + t.tileHeight);
                }
            }
        }

        if (!facing) {
            //Draw Selector
            int selectorOffset = (selectorCoords.y % 2 != 0) ? 32 : 0;
            Texture tempTexture = (selecting) ? selectingTexture : selectorTexture;
            int height = 0;
            if (doesTileExistThere(selectorCoords)) height = tileArray[selectorCoords.x][selectorCoords.y].tileHeight;
            sb.draw(tempTexture, (64 * selectorCoords.x) + selectorOffset, (16 * selectorCoords.y) + height);
        } else {
            int selectorOffset = (selectorCoords.y % 2 != 0) ? 32 : 0;
            int height = 0;
            if (doesTileExistThere(selectorCoords)) height = tileArray[selectorCoords.x][selectorCoords.y].tileHeight;
            height += 24;
            sb.draw(facingTexture, (64 * selectorCoords.x) + selectorOffset, (16 * selectorCoords.y) + height);
        }

        for (Character character:
             characterList) {
            Coordinates characterCoords = character.pos;

            //Draw Character
            int characterOffset = (characterCoords.y % 2 != 0) ? 32 : 0;
            if (character.img != null) {
                sb.draw(character.img, (64 * characterCoords.x) + characterOffset + 24, (16 * characterCoords.y) + 10 + tileArray[characterCoords.x][characterCoords.y].tileHeight);
            }
        }

    }

    void dispose () {

    }

    boolean doesTileExistThere (Coordinates tileIn) {
        boolean output = true;
        if ((tileIn.x >= width) || (tileIn.x < 0)) {
            return false;
        } else if ((tileIn.y >= height) || (tileIn.y < 0)) {
            return false;
        }
        if (!tileArray[tileIn.x][tileIn.y].enabled) return false;
        return output;
    }

    boolean doesCharacterExistThere (Coordinates tileIn) {
        for (Character c:
             characterList) {
            if ((tileIn.x == c.pos.x) && (tileIn.y == c.pos.y)) return true;
        }
        return false;
    }
    boolean doesCharacterExistThere (Coordinates tileIn, Character exception) {
        //Look to see if there's a character at the position you're looking EXCEPT if it's this character

        for (Character c:
                characterList) {
            if ((tileIn.x == c.pos.x) && (tileIn.y == c.pos.y)) {
                if ((exception.pos.x == tileIn.x) && (exception.pos.y == tileIn.y)) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public void disableTile(int x, int y) {
        //Turn off a tile
        if (doesTileExistThere(new Coordinates(x,y))) {
            tileArray[x][y].enabled = false;
        }
    }

    public void enableTile(int x, int y) {
        //Turn on a tile
        if (doesTileExistThere(new Coordinates(x,y))) {
            tileArray[x][y].enabled = true;
        }
    }

    public void setHeight (int x, int y, int z) {
        //Set what height a particular tile should be
        if (doesTileExistThere(new Coordinates(x,y))) {
            tileArray[x][y].tileHeight = z;
        }
    }
}
