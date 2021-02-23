/*/////////////////////////////////////////////////////
File: Pieces.java
Date: 4/17/2020
Classes: Pawn,King,Queen,Bishop,Rook,Knight
Total Method Count: 12
Written by: Ray Jerralds
Contributions: - 
/////////////////////////////////////////////////////*/

import java.util.Arrays;
import java.util.ArrayList;

class Pawn extends ChessPiece {
 //attacking will be done in board since it requires other pieces on the board
 //a special condition will be added for pawns in the filtering method where if conditions are met,
 //the respective attack positions will be added to their potential move positions 
  public Pawn(int team, int tag, String name, ArrayList<Integer>pos){
    super(team,tag,name,pos);
    setallmoves();
  }
   private void setallmoves(){
      ArrayList<ArrayList<Integer>>a=new ArrayList<ArrayList<Integer>>(); 
      int x=pos.get(0);
      int y=pos.get(1);
      int c;
      if (getTeam()==1){c=1;}
      else{c=-1;}
      if(getDidMove()==false){
        //1st move in their ranks, no need to check
        a.add(new ArrayList<Integer>(Arrays.asList(x,y+2*(c))));
      }
      if(y+1*(c)<=7){a.add(new ArrayList<Integer>(Arrays.asList(x,y+1*(c))));}
      moveslist=a;
   }
   public void updatePos(ArrayList<Integer>new_pos){
    if(pos.get(0)!=new_pos.get(0)|| pos.get(1)!=new_pos.get(1)){pieceMoved();}
    this.pos=new_pos;
    setallmoves();
   }
}

class King extends ChessPiece {
  public King(int team, int tag, String name, ArrayList<Integer>pos){
    super(team,tag,name,pos);
    setallmoves();
  }
  private void setallmoves(){
      ArrayList<ArrayList<Integer>>a=new ArrayList<ArrayList<Integer>>(); 
      int x=pos.get(0);
      int y=pos.get(1);
      //up-right
      if(x+1<=7 && y+1<=7){a.add(new ArrayList<Integer>(Arrays.asList(x+1,y+1)));}
      //up-left
      if(x-1>=0 && y+1<=7){a.add(new ArrayList<Integer>(Arrays.asList(x-1,y+1)));}
      //down-right
      if(x+1<=7 && y-1>=0){a.add(new ArrayList<Integer>(Arrays.asList(x+1,y-1)));}
      //down-left
      if(x-1>=0 && y-1>=0){a.add(new ArrayList<Integer>(Arrays.asList(x-1,y-1)));}
      //up
      if(y+1<=7){a.add(new ArrayList<Integer>(Arrays.asList(x,y+1)));}
      //down
      if(y-1>=0){a.add(new ArrayList<Integer>(Arrays.asList(x,y-1)));}
      //right
      if(x+1<=7){a.add(new ArrayList<Integer>(Arrays.asList(x+1,y)));}
      //left
      if(x-1>=0){a.add(new ArrayList<Integer>(Arrays.asList(x-1,y)));}
      moveslist=a;
  }
  public void updatePos(ArrayList<Integer>new_pos){
    if(pos.get(0)!=new_pos.get(0)|| pos.get(1)!=new_pos.get(1)){pieceMoved();}
    this.pos=new_pos;
    setallmoves();
  }
}

class Rook extends ChessPiece {
  public Rook(int team, int tag, String name, ArrayList<Integer>pos){
    super(team,tag,name,pos);
    setallmoves();
  }
  private void setallmoves(){
    ArrayList<ArrayList<Integer>>a=new ArrayList<ArrayList<Integer>>(); 
    int x=pos.get(0);
    int y=pos.get(1);
    int c=1;
    int size=a.size();
    while(true){
      //up
      if(y+c<=7){a.add(new ArrayList<Integer>(Arrays.asList(x,y+c)));}
      //down
      if(y-c>=0){a.add(new ArrayList<Integer>(Arrays.asList(x,y-c)));}
      //right
      if(x+c<=7){a.add(new ArrayList<Integer>(Arrays.asList(x+c,y)));}
      //left
      if(x-c>=0){a.add(new ArrayList<Integer>(Arrays.asList(x-c,y)));}
      c+=1;
      int b=a.size();
      if(b>size){size=b;continue;}
      else if (b==size){break;} 
    }
    moveslist=a;
  }
  public void updatePos(ArrayList<Integer>new_pos){
    if(pos.get(0)!=new_pos.get(0)|| pos.get(1)!=new_pos.get(1)){pieceMoved();}
    this.pos=new_pos;
    setallmoves();
  }
}


class Queen extends ChessPiece {
  public Queen(int team, int tag, String name, ArrayList<Integer>pos){
    super(team,tag,name,pos);
    setallmoves();
  }
   private void setallmoves(){
    ArrayList<ArrayList<Integer>>a=new ArrayList<ArrayList<Integer>>(); 
    int x=pos.get(0);
    int y=pos.get(1);
    int c=1;
    int size=a.size();
    while(true){
      //up-right
      if(x+c<=7 && y+c<=7){a.add(new ArrayList<Integer>(Arrays.asList(x+c,y+c)));}
      //up-left
      if(x-c>=0 && y+c<=7){a.add(new ArrayList<Integer>(Arrays.asList(x-c,y+c)));}
      //down-right
      if(x+c<=7 && y-c>=0){a.add(new ArrayList<Integer>(Arrays.asList(x+c,y-c)));}
      //down-left
      if(x-c>=0 && y-c>=0){a.add(new ArrayList<Integer>(Arrays.asList(x-c,y-c)));}
      //up
      if(y+c<=7){a.add(new ArrayList<Integer>(Arrays.asList(x,y+c)));}
      //down
      if(y-c>=0){a.add(new ArrayList<Integer>(Arrays.asList(x,y-c)));}
      //right
      if(x+c<=7){a.add(new ArrayList<Integer>(Arrays.asList(x+c,y)));}
      //left
      if(x-c>=0){a.add(new ArrayList<Integer>(Arrays.asList(x-c,y)));}
      c+=1;
      int b=a.size();
      if(b>size){size=b;continue;}
      else if (b==size){break;} 
   
    }
    moveslist=a;
  }
  public void updatePos(ArrayList<Integer>new_pos){
    if(pos.get(0)!=new_pos.get(0)|| pos.get(1)!=new_pos.get(1)){pieceMoved();}
    this.pos=new_pos;
    setallmoves();
  }
}


class Bishop extends ChessPiece {
  public Bishop(int team, int tag, String name, ArrayList<Integer>pos){
    super(team,tag,name,pos);
    setallmoves();
  }
  private void setallmoves(){
    ArrayList<ArrayList<Integer>>a=new ArrayList<ArrayList<Integer>>(); 
    int x=pos.get(0);
    int y=pos.get(1);
    int c=1;
    int size=a.size();
    while(true){
      //up-right
      if(x+c<=7 && y+c<=7){a.add(new ArrayList<Integer>(Arrays.asList(x+c,y+c)));}
      //up-left
      if(x-c>=0 && y+c<=7){a.add(new ArrayList<Integer>(Arrays.asList(x-c,y+c)));}
      //down-right
      if(x+c<=7 && y-c>=0){a.add(new ArrayList<Integer>(Arrays.asList(x+c,y-c)));}
      //down-left
      if(x-c>=0 && y-c>=0){a.add(new ArrayList<Integer>(Arrays.asList(x-c,y-c)));}
      c+=1;
      int b=a.size();
      if(b>size){size=b;continue;}
      else if (b==size){break;} 
   
    }
    moveslist=a;
  }
  public void updatePos(ArrayList<Integer>new_pos){
    if(pos.get(0)!=new_pos.get(0)|| pos.get(1)!=new_pos.get(1)){pieceMoved();}
    this.pos=new_pos;
    setallmoves();
  }
}




class Knight extends ChessPiece {
  public Knight(int team, int tag, String name, ArrayList<Integer>pos){
    super(team,tag,name,pos);
    setallmoves();
  }
  private void setallmoves(){
    ArrayList<ArrayList<Integer>>a=new ArrayList<ArrayList<Integer>>();
    int x=pos.get(0);
    int y=pos.get(1);
    //L up-right and L up-left
    if((x+1<=7 && y+2<=7)||(x-1>=0 && y+2<=7)){
      Boolean t1=(x+1<=7 && y+2<=7);
      Boolean t2=(x-1>=0 && y+2<=7);
      if(t1==true){a.add(new ArrayList<Integer>(Arrays.asList(x+1,y+2)));}
      if(t2==true){a.add(new ArrayList<Integer>(Arrays.asList(x-1,y+2)));}
    }
    //L down-right and L down-left
    if((x+1<=7 && y-2>=0)||(x-1>=0 && y-2>=0)){
      Boolean t1=(x+1<=7 && y-2>=0);
      Boolean t2=(x-1>=0 && y-2>=0);
      if(t1==true){a.add(new ArrayList<Integer>(Arrays.asList(x+1,y-2)));}
      if(t2==true){a.add(new ArrayList<Integer>(Arrays.asList(x-1,y-2)));}
    }
    //sideways L up-right and L up-left
    if((x+2<=7 && y+1<=7)||(x-2>=0 && y+1<=7)){
      Boolean t1=(x+2<=7 && y+1<=7);
      Boolean t2=(x-2>=0 && y+1<=7);
      if(t1==true){a.add(new ArrayList<Integer>(Arrays.asList(x+2,y+1)));}
      if(t2==true){a.add(new ArrayList<Integer>(Arrays.asList(x-2,y+1)));}
    }
    //sideways L down-right and L down-left
    if((x+2<=7 && y-1>=0)||(x-2>=0 && y-1>=0)){
      Boolean t1=(x+2<=7 && y-1>=0);
      Boolean t2=(x-2>=0 && y-1>=0);
      if(t1==true){a.add(new ArrayList<Integer>(Arrays.asList(x+2,y-1)));}
      if(t2==true){a.add(new ArrayList<Integer>(Arrays.asList(x-2,y-1)));}
    }
    moveslist=a;
  }
  public void updatePos(ArrayList<Integer>new_pos){
    if(pos.get(0)!=new_pos.get(0)|| pos.get(1)!=new_pos.get(1)){pieceMoved();}
    this.pos=new_pos;
    setallmoves();
  }
}