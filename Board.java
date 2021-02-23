import java.util.ArrayList;
import java.util.*;
import java.lang.Math;


class Run {
public static void main(String[] args){
 
  
  //Game Loop
  Scanner myObj = new Scanner(System.in);  // Create a Scanner object
  Board b= new Board();
  /*
  b.select("p1");
  ArrayList<Integer>coords=new ArrayList<Integer>();
  coords.add(0);
  coords.add(3);
  b.move(coords);
  
  b.select("p1");
  ArrayList<Integer>coords2=new ArrayList<Integer>();
  coords2.add(0);
  coords2.add(5);
  b.move(coords2);
  */
  
  while(true){
  System.out.println("Select piece to move: type e to exit");
  String s = myObj.nextLine();  // Reads user input to select
  if(s.equals("e")){break;}
  if(b.select(s)==null){System.out.println("Invalid entry"); continue;}
  //moves piece
  System.out.println("type position like [3,2] with no spaces 2.type e to exit, 3. type id to select different piece.");
  System.out.println("Where to?:");
  String s2 = myObj.nextLine(); 
  ArrayList<Integer>coords;
  
  try{
    String t1=s2.substring(1,2);
    String t2=s2.substring(3,4);
    int x=Integer.parseInt(t1);
    int y=Integer.parseInt(t2);
    coords=new ArrayList<Integer>();
    coords.add(x);
    coords.add(y);
  }
  
  catch(Exception e){System.out.println("Invalid Entry"); continue;}
  if(b.move(coords)==false){System.out.println("Invalid Coordinates"); continue;}
  else{
  System.out.println("Player"+b.getCurrentPlayer()+" it's your turn.");
  }
  
  }
  myObj.close();
 
  
  
  
  
  
  
 }

}

class Board{
  ChessPiece[][]cb= new ChessPiece[8][8];
  ChessPiece currentPiece;
  int currentPlayer;
  ArrayList<ArrayList<Integer>>validMoves;
  ArrayList<ChessPiece>removedPieces;
  boolean rankedUp;
  //check and checkMate fields
  ArrayList<ArrayList<Integer>>check_paths;
  ArrayList<ArrayList<Integer>>check_kingMoves;
  boolean in_check;
  boolean checkmate;
  int numPaths;
  
  public Board() {
   initialBoardSetup();
   currentPlayer=1;
   currentPiece=null;
   validMoves=null;
   check_paths=null;
   check_kingMoves=null;
   in_check=false;
   checkmate=false;
   rankedUp=false;
   numPaths=0;
   
   mechsOnly();
  
  }
 
  public void mechsOnly(){
   System.out.println("Welcome to CheckMate! Player "+currentPlayer+" it is your turn!\n");
   //testing
   //testBoard1();
   //testBoard2(); 
   //testBoard3();
   //testBoard4();
   //testBoard5();
   printBoard(currentPiece);
  }
  
  private void initialBoardSetup(){
    //intiates board to be all empty pieces
    removedPieces=new ArrayList<ChessPiece>();
    initiateBoard();
    setInitialPieces();
  }
  
  private void setInitialPieces(){
    //intializes all pawns for both players
    for(int i=0;i<cb.length;i++){
      cb[i][1]=new Pawn(1,i+1,"pawn",cb[i][1].getPos());
      cb[i][6]=new Pawn(2,i+1,"pawn",cb[i][6].getPos());
    } 
    //initalizes all other pieces for both players
    for(int i=0;i<cb.length;i++){
      //Rooks
      if(i==0){
        cb[i][0]=new Rook(1,1,"rook",cb[i][0].getPos());
        cb[i][7]=new Rook(2,1,"rook",cb[i][7].getPos());
        cb[7-i][i]=new Rook(1,2,"rook",cb[7-i][i].getPos());
        cb[7-i][7]=new Rook(2,2,"rook",cb[7-i][7].getPos());
      }
      //Knights
      if(i==1){
        cb[i][0]=new Knight(1,1,"knight",cb[i][0].getPos());
        cb[i][7]=new Knight(2,1,"knight",cb[i][7].getPos());
        cb[7-i][0]=new Knight(1,2,"knight",cb[7-i][0].getPos());
        cb[7-i][7]=new Knight(2,2,"knight",cb[7-i][7].getPos());
      }
      //Bishops
      if(i==2){
        cb[i][0]=new Bishop(1,1,"bishop",cb[i][0].getPos());
        cb[i][7]=new Bishop(2,1,"bishop",cb[i][7].getPos());
        cb[7-i][0]=new Bishop(1,2,"bishop",cb[7-i][0].getPos());
        cb[7-i][7]=new Bishop(2,2,"bishop",cb[7-i][7].getPos());
      } 
      //Queens
      if(i==3){
        cb[i][0]=new Queen(1,1,"queen",cb[i][0].getPos());
        cb[i][7]=new Queen(2,1,"queen",cb[i][7].getPos());
      } 
      //Kings
      if(i==4){
        cb[i][0]=new King(1,1,"king",cb[i][0].getPos());
        cb[i][7]=new King(2,1,"king",cb[i][7].getPos());
      }
    }
  }
  
  private void initiateBoard() {
    for(int i=0; i<cb.length;i++){
      for(int j=0;j < cb[i].length;j++){
        ArrayList<Integer> k= new ArrayList<Integer>();
        k.add(i);
        k.add(j);
        cb[i][j]=new ChessPiece(0,1,"empty",k);
      }
    }
  }
  
  public ArrayList<ArrayList<Double>>radiSupport(ArrayList<ArrayList<Integer>>list,ChessPiece c){
    ArrayList<ArrayList<Double>>angles=new ArrayList<ArrayList<Double>>();
    for(int i=0;i<list.size();i++){
      ArrayList<Double>a=new ArrayList<Double>();
      float x=list.get(i).get(0)-c.getPos().get(0);
      float y=list.get(i).get(1)-c.getPos().get(1);
      double r=Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
      a.add(x/r);
      a.add(y/r);
      a.add(r);
      angles.add(a); 
    }
    return angles;
  }
  
  public boolean accuracy(double a, double b){
    double c=b-a;
    if(c<0){c*=-1;}
    if(c<0.0000000000000009 || c==0){return true;}
    return false;
  }
  
  public ArrayList<ArrayList<Integer>>filtered(ChessPiece c, int player, ArrayList<Integer>remove_special_case){
    if(c==null){return null;}
    ArrayList<ArrayList<Integer>> vm= new ArrayList<ArrayList<Integer>>(c.getallmoves());
    ArrayList<ArrayList<Double>>radiList=radiSupport(vm,c);
    ArrayList<ArrayList<Integer>>white=new ArrayList<ArrayList<Integer>>();
    ArrayList<ArrayList<Integer>>black=new ArrayList<ArrayList<Integer>>();
    for(int i=0;i<cb.length;i++){
      for(int j=0;j<cb[i].length;j++){
        if(cb[i][j].getName()=="empty"){continue;}
        if(cb[i][j].getTeam()==1&&vm.contains(cb[i][j].getPos())){white.add(cb[i][j].getPos());
        continue;}
        if(cb[i][j].getTeam()==2&&vm.contains(cb[i][j].getPos())){black.add(cb[i][j].getPos());
        continue;}
      }
    }
    //special case for checking if piece can move(conditional)
    if(remove_special_case!=null){
      if(player==1){black.remove(remove_special_case);}
      else{white.remove(remove_special_case);}
    }
    //special cases knights/pawns
    if(c.getName()=="knight"){
      if(player==2){vm.removeAll(black);}
      else{vm.removeAll(white);}
      return vm;
    }
    if(c.getName()=="pawn"){
      int x=c.getPos().get(0);
      int y=c.getPos().get(1);
      int cc=1;
      if(player==2){cc=-1;}
      //removes pieces in front of pawn from moves
      vm.removeAll(white);
      vm.removeAll(black);
      if(c.getDidMove()==false&&vm.contains(new ArrayList<Integer>(Arrays.asList(x,y+cc)))==false){
        vm.remove(new ArrayList<Integer>(Arrays.asList(x,y+(2*cc))));}
     
      //attacks
      if(y+cc<=7&&y+cc>=0){
        if(x-1>=0&&pieceAt(new ArrayList<Integer>(Arrays.asList(x-1,y+cc))).getTeam()!=player
          &&pieceAt(new ArrayList<Integer>(Arrays.asList(x-1,y+cc))).getTeam()!=0){
          vm.add(new ArrayList<Integer>(Arrays.asList(x-1,y+cc)));}
        if(x+1<=7&&pieceAt(new ArrayList<Integer>(Arrays.asList(x+1,y+cc))).getTeam()!=player
          &&pieceAt(new ArrayList<Integer>(Arrays.asList(x+1,y+cc))).getTeam()!=0){
          vm.add(new ArrayList<Integer>(Arrays.asList(x+1,y+cc)));}
       
      }
  
      ArrayList<Integer>nu=new ArrayList<Integer>();
      nu.add(null);
      vm.removeAll(nu);
      return vm;
      
    }
    
    
    
    //switches friendly/enemy for each player(radiList)
    ArrayList<ArrayList<Double>>whiteR;
    ArrayList<ArrayList<Double>>blackR;
    if(player==2){
      whiteR=radiSupport(black,c);
      blackR=radiSupport(white,c);
    }
    else{
    whiteR=radiSupport(white,c);
    blackR=radiSupport(black,c);}
    
    //friendly filter
    for(int i=0;i<radiList.size();i++){
      for(int j=0;j<whiteR.size();j++){
        if(accuracy(radiList.get(i).get(0),whiteR.get(j).get(0))&& 
           accuracy(radiList.get(i).get(1),whiteR.get(j).get(1)))
        {
          if(radiList.get(i).get(2)>=whiteR.get(j).get(2)){vm.set(i,null);}
        }
      }
    }
    //enemy filter
    for(int i=0;i<blackR.size();i++){
      for(int j=0;j<radiList.size();j++){
        if(pieceAt(vm.get(j))==null || pieceAt(vm.get(j)).getName()=="empty"){continue;}
        if(accuracy(blackR.get(i).get(0),radiList.get(j).get(0))&&
           accuracy(blackR.get(i).get(1),radiList.get(j).get(1))){
          if(blackR.get(i).get(2)<radiList.get(j).get(2)){vm.set(j,null);}
        }
      }
    }
    ArrayList<Integer>nu=new ArrayList<Integer>();
    nu.add(null);
    vm.removeAll(nu);
    return vm;
  }
  
  public boolean move(ArrayList<Integer> pos){
    if(currentPiece==null||validMoves==null){return false;}
    if(!validMoves.contains(pos)){return false;}
    
    if(currentPiece.getName().equals("king")){//special case king castles
      //right castle
      if(pos.get(0)-2==currentPiece.getPos().get(0)){
        String s="r2-"+currentPlayer;
        ChessPiece temp1=pieceAt(getLocation(s));
        swap(temp1.getPos(),new ArrayList<Integer>(Arrays.asList(pos.get(0)-1,pos.get(1))));
      }
      //left castle
      else if(pos.get(0)-2==0){
        String s="r1-"+currentPlayer;
        ChessPiece temp1=pieceAt(getLocation(s));
        swap(temp1.getPos(),new ArrayList<Integer>(Arrays.asList(pos.get(0)+1,pos.get(1))));
      }
      
    }
    
    swap(currentPiece.getPos(),pos);
    if(currentPiece.getName()=="pawn"){checkRankedUp(currentPiece);}//GRAPHICS has to prompt for string before continuing
    printBoard(null);
    check_RESET();
    updateTurn();
    return true; 
  }
  
  public ChessPiece select(String piece){
    //regular movement (Not in check)
    if(in_check==false){
      String s=piece+"-"+currentPlayer;
      boolean found=false;
      for(int i=0;i<cb.length;i++){
        if(found==true){break;}
        for(int j=0;j<cb[i].length;j++){
          if(cb[i][j].getId().equals(s)){
            //checks if piece can move without putting king in check first
            if(!check_IfcanMove(cb[i][j].getPos())){return null;}
            currentPiece=cb[i][j];
            found=true;
            break;
          }
        }
      }
      if(found==false){return null;}
      
      
      //check for castling
      if(piece.equals("*1")&&currentPiece.getDidMove()==false &&in_check==false){
        validMoves=kingcanCastle(filtered(currentPiece,getCurrentPlayer(),null));
      }
      else{validMoves=filtered(currentPiece,getCurrentPlayer(),null);}
      
      //checks if king can freely moves itself into check, castling
      if(piece.equals("*1")){validMoves=check_kingPlaysHimself(validMoves);}
      
      printBoard(currentPiece);
      return currentPiece;
      
    }
    //in check and king has to run
    else if(numPaths>1){
      String s="*1"+"-"+getCurrentPlayer();
      boolean found=false;
      for(int i=0;i<cb.length;i++){
        if(found==true){break;}
        for(int j=0;j<cb[i].length;j++){
          if(cb[i][j].getId().equals(s)){
            currentPiece=cb[i][j];
            found=true;
            break;
          }
        }
    }
    validMoves=check_kingMoves;
    printBoard(currentPiece);
    return currentPiece;
    }
    
    //only in a one path check
    else if(numPaths==1){
      //shows the set of possible positions to get out of check, without moving king
      if(!piece.equals("*1")){
      ArrayList<String>listOfPieces=new ArrayList<String>();
      for(int i=0;i<cb.length;i++){
        for(int j=0;j<cb[i].length;j++){
          if(cb[i][j].getTeam()!=getCurrentPlayer()){continue;}
          if(set_intersect(filtered(cb[i][j],getCurrentPlayer(),null),check_paths).size()>0){
            listOfPieces.add(cb[i][j].getId());
          }
        }
      }
      if(!listOfPieces.contains(piece+"-"+currentPlayer)){return null;}
      boolean found=false;
      for(int i=0;i<cb.length;i++){
        if(found==true){break;}
        for(int j=0;j<cb[i].length;j++){
          if(cb[i][j].getId().equals(piece+"-"+currentPlayer)){
            currentPiece=cb[i][j];
            found=true;
            break;
          }
        }
      }
      validMoves=set_intersect(check_paths,filtered(currentPiece,currentPlayer,null));
      printBoard(currentPiece);
      System.out.println(listOfPieces);
      return currentPiece;
    }
    //if king is chosen to move
      else{
        String s="*1"+"-"+getCurrentPlayer();
        boolean found=false;
        for(int i=0;i<cb.length;i++){
          if(found==true){break;}
          for(int j=0;j<cb[i].length;j++){
            if(cb[i][j].getId().equals(s)){
              currentPiece=cb[i][j];
              found=true;
              break;
            }
          }
        }
        validMoves=check_kingMoves;
        printBoard(currentPiece);
        return currentPiece;
        
      }
    
      
    }   
      
    return null;
  }
  
  public void updateTurn(){
    //checks if next player is in check
    int nextPlayer;
    if(getCurrentPlayer()==1){nextPlayer=2;}
    else{nextPlayer=1;}
    ArrayList<Integer> kings_pos=new ArrayList<Integer>();
    boolean found=false;
    //check if king is in check
    for(int i=0;i<cb.length;i++){
      if(found==true){break;}
      for(int j=0;j<cb[i].length;j++){
        if(cb[i][j].getTeam()==nextPlayer &&cb[i][j].getName().equals("king")){
        kings_pos.add(cb[i][j].getPos().get(0));
        kings_pos.add(cb[i][j].getPos().get(1));
        found=true; 
        break;
        }
      }
    } 
    if(found==false){changePlayer();return;}
    //finds pieces putting king in check
    check_paths=new ArrayList<ArrayList<Integer>>();
    for(int i=0;i<cb.length;i++){
      for(int j=0;j<cb[i].length;j++){
        if(cb[i][j].getName().equals("empty")){continue;}
        if(cb[i][j].getTeam()!=getCurrentPlayer()){continue;}
        
        if(filtered(cb[i][j],getCurrentPlayer(),null).contains(kings_pos)){
          in_check=true;
          ArrayList<ArrayList<Integer>> path=getPath(cb[i][j],kings_pos);
          path.remove(kings_pos);
          path.add(cb[i][j].getPos());
          check_paths.addAll(path); 
          numPaths++;
        }
      }
    }
    //System.out.println(check_paths);  
    //goes to next player
    check_Checkmate(kings_pos,nextPlayer);
    
    
    
    changePlayer();
  }
  
  
  public void check_RESET(){
    in_check=false;
    check_paths=null;
    check_kingMoves=null;
    check_paths=null;
    numPaths=0;
  }
  
  public ArrayList<ArrayList<Integer>> check_kingPlaysHimself(ArrayList<ArrayList<Integer>>king_filtered){
    int nextplayer;
    if(getCurrentPlayer()==1){nextplayer=2;}
    else{nextplayer=1;}
    for(int i=0;i<cb.length;i++){
      for(int j=0;j<cb[i].length;j++){
        if(cb[i][j].getName().equals("empty")){continue;}
        if(cb[i][j].getTeam()==currentPlayer){continue;}
        ArrayList<ArrayList<Integer>>temp=filtered(cb[i][j],nextplayer,null);
        ArrayList<ArrayList<Integer>>intersected=set_intersect(temp,king_filtered);
        if(intersected.size()>0){king_filtered.removeAll(intersected);}
      }
      
    }
    return king_filtered;
  }
  
  public boolean check_IfcanMove(ArrayList<Integer>pos){
    int nextplayer;
    if(getCurrentPlayer()==1){nextplayer=2;}
    else{nextplayer=1;}
    String current_king="*1-"+getCurrentPlayer();
   
    
    for(int i=0;i<cb.length;i++){
      for(int j=0;j<cb[i].length;j++){
        if(cb[i][j].getName().equals("empty")){continue;}
        if(cb[i][j].getTeam()==getCurrentPlayer()){continue;}
        if(filtered(cb[i][j],nextplayer,pos).contains(getLocation(current_king))){
          return false;
        }
      }
    }
    
    
    
    
    
    return true;
  }
  
  
  public boolean check_Checkmate(ArrayList<Integer>kingsPos,int nextplayer){
    check_kingMoves=check_setKingMoves(kingsPos,nextplayer);
    if(in_check==true&&check_kingMoves==null){System.out.println("Checkmate!!");checkmate=true;return true;}
    return false;
  }
  public ArrayList<ArrayList<Integer>> check_setKingMoves(ArrayList<Integer>kingsPos,int nextplayer){
    //checks for opposite team to compare to without changing player in system
    ArrayList<ArrayList<Integer>>contAttacks=new ArrayList<ArrayList<Integer>>();
    ArrayList<ArrayList<Integer>>pawnAttacks=new ArrayList<ArrayList<Integer>>();
    
    for(int i=0;i<cb.length;i++){
      for(int j=0;j<cb[i].length;j++){
        if(cb[i][j].getTeam()!=getCurrentPlayer()){continue;}
        if(!cb[i][j].getName().equals("pawn")){
          contAttacks=set_union(contAttacks,filtered(cb[i][j],getCurrentPlayer(),null));
        }
        //accounts for potential pawn attacks
        else{
          int c=1;
          if(getCurrentPlayer()==2){c*=-1;}
          int x=cb[i][j].getPos().get(0);
          int y=cb[i][j].getPos().get(1);
          ArrayList<ArrayList<Integer>>temp =new ArrayList<ArrayList<Integer>>();
          if(y+c<=7&&y+c>=0){
            if(x-1>=0){temp.add(new ArrayList<Integer>(Arrays.asList(x-1,y+c)));}
            if(x+1<=7){temp.add(new ArrayList<Integer>(Arrays.asList(x+1,y+c)));}
          }
          pawnAttacks=set_union(pawnAttacks,temp);
        }
        
        
        
      }
      
    }
    
    contAttacks=set_union(contAttacks,pawnAttacks);
    contAttacks.remove(kingsPos);
    
    ArrayList<ArrayList<Integer>>valid_king_moves=filtered(pieceAt(kingsPos),nextplayer,null);
    
    valid_king_moves.removeAll(set_intersect(contAttacks,valid_king_moves));
    //System.out.println(valid_king_moves);
    if(valid_king_moves.size()==0){return null;}
    
    return valid_king_moves;
  }
  
  
  //Supporters
 
  public ArrayList<ArrayList<Integer>>getPath(ChessPiece piece, ArrayList<Integer> pos){
   ArrayList<ArrayList<Double>>rads=radiSupport(piece.getallmoves(),piece);
   ArrayList<ArrayList<Integer>>pathList=new ArrayList<ArrayList<Integer>>();
   int index=piece.getallmoves().indexOf(pos);
   for(int i=0;i<rads.size();i++){
     if(accuracy(rads.get(i).get(0),rads.get(index).get(0))&&accuracy(rads.get(i).get(1),rads.get(index).get(1))){
       pathList.add(piece.getallmoves().get(i));
     }
   }
   return pathList; 
  }

  public boolean checkRankedUp(ChessPiece pawn){
    int y;
    if(currentPlayer==1){y=7;}
    else{y=0;}
    if(pawn.getPos().get(1)==y){
      rankedUp=true;
      rankUp("queen");
      return true;
    }
    return false;
  }
  
  
  public void rankUp(String s){
    //s will be the desired ranked piece's name all lowercase
    int queenMAXTags=0;
    int rookMAXTags=0;
    int bishopMAXTags=0;
    int knightMAXTags=0;
    ChessPiece newPiece=new ChessPiece(0,1,"empty",currentPiece.getPos());
    
    for(int i=0;i<cb.length;i++){
      for(int j=0;j<cb[i].length;j++){
        if(cb[i][j].getTeam()!=currentPlayer){continue;}
        if(cb[i][j].getName().equals("queen")){queenMAXTags++;}
        if(cb[i][j].getName().equals("rook")){rookMAXTags++;}
        if(cb[i][j].getName().equals("bishop")){bishopMAXTags++;}
        if(cb[i][j].getName().equals("knight")){knightMAXTags++;}
      }
    }
    if(s.equals("queen")){
      newPiece=new Queen(currentPlayer,queenMAXTags+1,s,currentPiece.getPos());
      currentPiece=newPiece;
    }
    if(s.equals("rook")){
      newPiece=new Rook(currentPlayer,rookMAXTags+1,s,currentPiece.getPos());
      currentPiece=newPiece;
    }
    if(s.equals("bishop")){
      newPiece=new Bishop(currentPlayer,bishopMAXTags+1,s,currentPiece.getPos());
      currentPiece=newPiece;
    }
    if(s.equals("knight")){
      newPiece=new Knight(currentPlayer,knightMAXTags+1,s,currentPiece.getPos());
      currentPiece=newPiece;
    }
    else{newPiece=currentPiece;}
    
    
    cb[currentPiece.getPos().get(0)][currentPiece.getPos().get(1)]=currentPiece;
    currentPiece.printInfo();
   
  }
  
  public ArrayList<ArrayList<Integer>> kingcanCastle(ArrayList<ArrayList<Integer>>king_filtered){
    String rook1="r1-"+currentPlayer;
    String rook2="r2-"+currentPlayer;
    ChessPiece tempR1=pieceAt(getLocation(rook1));
    ChessPiece tempR2=pieceAt(getLocation(rook2));
    
    //right castle
    if(tempR2.getDidMove()==false){
      ChessPiece spot1=pieceAt(new ArrayList<Integer>
      (Arrays.asList(currentPiece.getPos().get(0)+1,currentPiece.getPos().get(1))));
      ChessPiece spot2=pieceAt(new ArrayList<Integer>
      (Arrays.asList(currentPiece.getPos().get(0)+2,currentPiece.getPos().get(1))));
      if(spot1.getName().equals("empty")&&spot2.getName().equals("empty")){
        king_filtered.add(new ArrayList<Integer>
        (Arrays.asList(currentPiece.getPos().get(0)+2,currentPiece.getPos().get(1))));
      }                
    }
      
    //left castle
    if(tempR1.getDidMove()==false){
      ChessPiece spot1=pieceAt(new ArrayList<Integer>
      (Arrays.asList(currentPiece.getPos().get(0)-1,currentPiece.getPos().get(1))));
      ChessPiece spot2=pieceAt(new ArrayList<Integer>
      (Arrays.asList(currentPiece.getPos().get(0)-2,currentPiece.getPos().get(1))));
      ChessPiece spot3=pieceAt(new ArrayList<Integer>
      (Arrays.asList(currentPiece.getPos().get(0)-3,currentPiece.getPos().get(1))));
      if(spot1.getName().equals("empty")&&spot2.getName().equals("empty")&&spot3.getName().equals("empty")){
        king_filtered.add(new ArrayList<Integer>
        (Arrays.asList(currentPiece.getPos().get(0)-2,currentPiece.getPos().get(1))));
      }                
    }
    



    return king_filtered;
  }
  
  
  
  
  public ChessPiece pieceAt(ArrayList<Integer> pos){
    ChessPiece temp=null;
    boolean found=false;
    for(int i=0;i<cb.length;i++){
      if(found==true){break;}
      for(int j=0;j<cb[i].length;j++){
        if(cb[i][j].getPos().equals(pos)){
          temp=cb[i][j];
          found=true;
          break;
        }
      }
    }
    return temp;
  }
  
  
  
  public void swap(ArrayList<Integer> pos1,ArrayList<Integer> pos2){
    //swaps chess pieces based of given positions
    //will handle attacks using removePiece
    ChessPiece p1=pieceAt(pos1); 
    ChessPiece p2=pieceAt(pos2);
   
    /*
    //if piece is attacked
    if(p1.getTeam()!=0 && p2.getTeam()!=0){
      if(p1.getTeam()==1){p2=removePiece(p2.getId());}
      else{p1=removePiece(p1.getId());}
    }*/
    p1.updatePos(pos2);
    p2.updatePos(pos1);
    cb[pos1.get(0)][pos1.get(1)]=p2;
    cb[pos2.get(0)][pos2.get(1)]=p1;
    
    if(p2.getTeam()!=currentPlayer){removePiece(cb[pos1.get(0)][pos1.get(1)].getId());}
    else{removePiece(cb[pos2.get(0)][pos2.get(1)].getId());}
    
  }
  
  public ChessPiece removePiece(String id){
     ChessPiece empty_temp=new ChessPiece(0,1,"empty",new ArrayList<Integer>(Arrays.asList(0,0)));
     boolean found=false;
     for(int i=0;i<cb.length;i++){
       if(found==true){break;}
       for(int j=0;j<cb[i].length;j++){
         if(cb[i][j].getId().equals(id)){
           removedPieces.add(cb[i][j]);
           empty_temp.updatePos(new ArrayList<Integer>(Arrays.asList(cb[i][j].getPos().get(0),cb[i][j].getPos().get(1))));
           cb[i][j]=empty_temp;
           found=true;
           break;
         }
       }
     }
    System.out.println(removedPieces);
    return empty_temp;
  }
  
  //written by: Ali Asif
  public ArrayList<ArrayList<Integer>> set_intersect(ArrayList<ArrayList<Integer>>A,ArrayList<ArrayList<Integer>>B){
    ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
    for(int i = 0; i < A.size(); i++){
      if(B.contains(A.get(i))){result.add(A.get(i));}
    }
    return result;
  }
  //written by: Ali Asif
  public ArrayList<ArrayList<Integer>> set_union(ArrayList<ArrayList<Integer>>A,ArrayList<ArrayList<Integer>>B){
    ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
    for(int i = 0; i < A.size(); i++){result.add(A.get(i));}
    for(int j = 0; j < B.size(); j++){
      if(!result.contains(B.get(j))){result.add(B.get(j));}
    }
    return result;
  }
  
  
  public void changePlayer(){
    currentPlayer+=1;
    if(currentPlayer>2){currentPlayer=1;}
    currentPiece=null;
    validMoves=null;
    rankedUp=false;
    
    System.out.println("In check- "+in_check);
  } 
  
  
  
  
  public int getCurrentPlayer(){return currentPlayer;}
  public ArrayList<ArrayList<Integer>>getvalidMoves(){return validMoves;}
  public void setCurrentPiece(ChessPiece c){currentPiece=c;}
  public boolean getRankUp(){return rankedUp;}
 
  
  public String printBoard(ChessPiece p) {
   String result;
   if(p==null){result = "Board:\n";}
   else{result = "Board:                           Possible moves: \n";}
   for(int x = cb.length-1; x >= 0; x--) {
    result += "";
    for(int y = 0; y < cb[x].length; y++) {
     result += cb[y][x].getPrintId();
    }
    if(p!=null){
    result += "    |    ";
    for(int y = 0; y < cb[x].length; y++) {
      boolean loc=false;
      for(int i=0;i<validMoves.size();i++){
        
        if(validMoves.get(i).equals(cb[y][x].getPos())){
          result +="(X)";
          loc=true;
        }
      }
      if(loc==false){
      result += cb[y][x].getPrintId();
      }
    }
    }
    result += "\n";
   }
   System.out.println(result);
   return result;
  }
  
  //Graphics 
  public ArrayList<ArrayList<Integer>> getPossibleMoves(String id){
    boolean found=false;
    for(int i=0;i<cb.length;i++){
      if(found==true){break;}
      for(int j=0;j<cb[i].length;j++){
        if(cb[i][j].getId().equals(id)){
          found=true;
          return cb[i][j].getallmoves();}
      }
    }
    return null;
  }
  public ArrayList<Integer> getLocation(String id){
    for(int i=0;i<cb.length;i++){
      for(int j=0;j<cb[i].length;j++){
        if(cb[i][j].getId().equals(id)){
          return cb[i][j].getPos();}
      }
    }
    return null;
  }
  public boolean get_checkmate(){return checkmate;}
  public boolean get_check(){return in_check;}
  //public boolean check(int team){return false;}
  public boolean possibleSelect(int team, ArrayList<Integer> start){return false;}
  public int move(ArrayList<Integer> start,ArrayList<Integer> destination){return 0;}
  
  
  //Test
  public void testBoard1(){
  //tests for 1 path check
  ArrayList<Integer> p1= new ArrayList<Integer>(Arrays.asList(2,1));
  ArrayList<Integer> p2= new ArrayList<Integer>(Arrays.asList(2,2));
  swap(p1,p2);
  p1= new ArrayList<Integer>(Arrays.asList(5,6));
  p2= new ArrayList<Integer>(Arrays.asList(5,4));
  swap(p1,p2);
  p1= new ArrayList<Integer>(Arrays.asList(6,0));
  p2= new ArrayList<Integer>(Arrays.asList(7,2));
  swap(p1,p2);
  p1= new ArrayList<Integer>(Arrays.asList(6,7));
  p2= new ArrayList<Integer>(Arrays.asList(5,5));
  swap(p1,p2);
  p1= new ArrayList<Integer>(Arrays.asList(1,0));
  p2= new ArrayList<Integer>(Arrays.asList(0,2));
  swap(p1,p2);
  p1= new ArrayList<Integer>(Arrays.asList(3,6));
  p2= new ArrayList<Integer>(Arrays.asList(3,5));
  swap(p1,p2);
  return;
  }
  
  public void testBoard2(){
  //tests for multi-path check
   ArrayList<Integer> p1= new ArrayList<Integer>(Arrays.asList(5,0));
   ArrayList<Integer> p2= new ArrayList<Integer>(Arrays.asList(4,1));
   removePiece("p1-1");
   removePiece("p5-1");
   removePiece("p4-2");
   removePiece("p5-2");
   removePiece("p6-2");
   removePiece("p8-2");
   removePiece("k2-1");
   swap(p1,p2);
   p1=new ArrayList<Integer>(Arrays.asList(4,0));
   p2=new ArrayList<Integer>(Arrays.asList(6,0));
   swap(p1,p2);
   p1=new ArrayList<Integer>(Arrays.asList(0,0));
   p2=new ArrayList<Integer>(Arrays.asList(4,0));
   swap(p1,p2);
   //System.out.println(removedPieces);
 
  return;
  }
  public void testBoard3(){
  //tests checkmate
   ArrayList<Integer> p1= new ArrayList<Integer>(Arrays.asList(5,0));
   ArrayList<Integer> p2= new ArrayList<Integer>(Arrays.asList(4,1));
   removePiece("p1-1");
   removePiece("p5-1");
   removePiece("p4-2");
   removePiece("p5-2");
   removePiece("p6-2");
   removePiece("p8-2");
   removePiece("k2-1");
   
   swap(p1,p2);
   p1=new ArrayList<Integer>(Arrays.asList(4,0));
   p2=new ArrayList<Integer>(Arrays.asList(6,0));
   swap(p1,p2);
   p1=new ArrayList<Integer>(Arrays.asList(0,0));
   p2=new ArrayList<Integer>(Arrays.asList(4,0));
   swap(p1,p2);
   p1=new ArrayList<Integer>(Arrays.asList(6,5));
   p2=new ArrayList<Integer>(Arrays.asList(6,1));
   swap(p1,p2);
   //System.out.println(removedPieces);
 
  return;
  }
  public void testBoard4(){
  //tests for currentPlayer putting self in check 
   ArrayList<Integer> p1= new ArrayList<Integer>(Arrays.asList(2,1));
   ArrayList<Integer> p2= new ArrayList<Integer>(Arrays.asList(2,2));
   swap(p1,p2);
   p1=new ArrayList<Integer>(Arrays.asList(7,6));
   p2=new ArrayList<Integer>(Arrays.asList(7,5));
   swap(p1,p2);
  return;
  }
  public void testBoard5(){
  //casteling 
   removePiece("p2-2");
   removePiece("k1-1");
   removePiece("k2-1");
   removePiece("q1-1");
   removePiece("b1-1");
   removePiece("b2-1");
   removePiece("p5-1");
   ArrayList<Integer> p1= new ArrayList<Integer>(Arrays.asList(2,7));
   ArrayList<Integer> p2= new ArrayList<Integer>(Arrays.asList(0,5));
   swap(p1,p2);
   System.out.println(removedPieces);
  return;
  }
  
}











