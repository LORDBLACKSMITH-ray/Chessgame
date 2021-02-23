/*/////////////////////////////////////////////////////
File: ChessPiece.java
Date: 4/17/2020
Classes: ChessPiece
Total Method Count: 13 
Written by: Ray Jerralds 
Contributions: - 
/////////////////////////////////////////////////////*/

import java.util.ArrayList;

class ChessPiece {
  private int tag; //count id, only 1-2 or for pawns 1-8
  private int team; //only 0-2 Player 1=1 Player 2=2 Empty=0
  private String name,id,vis_id;
  private boolean moved;
  public ArrayList<Integer> pos;
  public ArrayList<ArrayList<Integer>>moveslist;
  
  public ChessPiece(int team, int tag, String name, ArrayList<Integer>pos){
    this.team=team;
    this.tag=tag;
    this.name=name;
    this.pos=pos;
    moved=false;
    vis_id=setPrintId();
    id=setId();
    setallmoves();
  }
  private void setallmoves(){
    ArrayList<ArrayList<Integer>>a=new ArrayList<ArrayList<Integer>>();
    a.add(new ArrayList<Integer>(pos));
    moveslist=a;
  }
  public void printInfo(){
    System.out.println("Team: "+team+ "\nName: " +name+"\npos: "+pos+"\nunique ID: "+id
                      +"\nPrint Visual: "+vis_id+"\npotential moves:"+moveslist+"\nmoved: "+getDidMove());  
  } 
  private String setId(){
    char[] temp=new char[4];
    if(name=="king"){temp[0]=vis_id.charAt(0);}
    else{temp[0]=name.charAt(0);}
    temp[1]=(char)(tag+'0');
    temp[2]='-';
    temp[3]=(char)(team+'0'); 
    String s= new String(temp);
    return s;
  }
  public void updatePos(ArrayList<Integer>new_pos){
    if(pos.get(0)!=new_pos.get(0)|| pos.get(1)!=new_pos.get(1)){pieceMoved();}
    this.pos=new_pos;
    setallmoves();
  }
  public ArrayList<ArrayList<Integer>>getallmoves(){return moveslist;}
  public String setPrintId(){
    if(name=="empty"){return " - ";}
    else if (team==1){
      if(name=="king"){return "*"+team+" ";}
       char[] temp=new char[2];
       temp[0]=name.charAt(0);
       temp[1]=(char)(tag+'0');
       String s= new String(temp);
       s+=" ";
       return s.toUpperCase();
    }
    else if (team==2){
       if(name=="king"){return "*"+team+" ";}
       char[] temp=new char[2];
       temp[0]=name.charAt(0);
       temp[1]=(char)(tag+'0');
       String s= new String(temp);
       s+=" ";
       return s.toLowerCase();
    } 
    
    return null;
  }
  public String getId(){return id;}
  public String getPrintId(){return vis_id;}
  public String getName(){return name;}
  public int getTag(){return tag;}
  public void pieceMoved(){moved=true;}
  public boolean getDidMove(){return moved;}
  public int getTeam(){return team;}
  public ArrayList<Integer> getPos(){return pos;}
}
