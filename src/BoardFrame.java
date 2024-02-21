import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import github.goxjanskloon.logicblocks.Board;
import github.goxjanskloon.logicblocks.Board.Block;
public class BoardFrame extends JFrame{
    private class BlockModifyListener implements Board.ModifyListener<BlockModifyListener>{
        private static AtomicInteger count=new AtomicInteger(0);
        public final int id=count.getAndIncrement();
        public int compareTo(BlockModifyListener ml){return Integer.valueOf(id).compareTo(ml.id);}
        public void modifyBlock(Block block){
            int xl=-xOffset/blockSize,yl=-yOffset/blockSize,xr=xl+getWidth()/blockSize,yr=yl+getHeight()/blockSize;
            if(xl<0) xl=0;if(yl<0) yl=0;if(xr>=board.getWidth()) xr=board.getWidth()-1;if(yr>=board.getHeight()) yr=board.getHeight()-1;
            if(block.x<xl||xr<block.x||block.y<yl||yr<block.y) return;
            paint(block);
    }}
    private static BufferedImage[][] IMAGES=null;
    private static BufferedImage[][][][][][] LINE_IMAGES=null;
    private Board board;
    private Graphics graphics=null;
    private int xOffset=0,yOffset=0,blockSize=50,choosedType=0,xOfsOrg=0,yOfsOrg=0;
    private File file=null;
    private static BufferedImage readImage(String path)throws IOException{
        return ImageIO.read(Board.class.getClassLoader().getResource(path));
    }
    static{try{
        IMAGES=new BufferedImage[][]{
        {readImage("img/VOID0.png"),readImage("img/VOID1.png")},
        {readImage("img/OR0.png"),readImage("img/OR1.png")},
        {readImage("img/NOT0.png"),readImage("img/NOT1.png")},
        {readImage("img/AND0.png"),readImage("img/AND1.png")},
        {readImage("img/XOR0.png"),readImage("img/XOR1.png")},
        {readImage("img/SRC0.png"),readImage("img/SRC1.png")}};
        LINE_IMAGES=new BufferedImage[][][][][][]{
        {{{{{readImage("img/LINEU0.png"),readImage("img/LINEU1.png")},
        {readImage("img/LINEUR0.png"),readImage("img/LINEUR1.png")}},
        {{readImage("img/LINEUL0.png"),readImage("img/LINEUL1.png")},
        {readImage("img/LINEULR0.png"),readImage("img/LINEULR1.png")}}},{{{null,null},{null,null}},{{null,null},{null,null}}}},
        {{{{readImage("img/LINEUU0.png"),readImage("img/LINEUU1.png")},
        {readImage("img/LINEUUR0.png"),readImage("img/LINEUUR1.png")}},
        {{readImage("img/LINEUUL0.png"),readImage("img/LINEUUL1.png")},
        {readImage("img/LINEUULR0.png"),readImage("img/LINEUULR1.png")}}},{{{null,null},{null,null}},{{null,null},{null,null}}}}},
        {{{{{readImage("img/LINED0.png"),readImage("img/LINED1.png")},
        {readImage("img/LINEDR0.png"),readImage("img/LINEDR1.png")}},
        {{readImage("img/LINEDL0.png"),readImage("img/LINEDL1.png")},
        {readImage("img/LINEDLR0.png"),readImage("img/LINEDLR1.png")}}},
        {{{readImage("img/LINEDD0.png"),readImage("img/LINEDD1.png")},
        {readImage("img/LINEDDR0.png"),readImage("img/LINEDDR1.png")}},
        {{readImage("img/LINEDDL0.png"),readImage("img/LINEDDL1.png")},
        {readImage("img/LINEDDLR0.png"),readImage("img/LINEDDLR1.png")}}}},{{{{null,null},{null,null}},{{null,null},{null,null}}},{{{null,null},{null,null}},{{null,null},{null,null}}}}},
        {{{{{readImage("img/LINEL0.png"),readImage("img/LINEL1.png")},{null,null}},
        {{readImage("img/LINELL0.png"),readImage("img/LINELL1.png")},{null,null}}},
        {{{readImage("img/LINELD0.png"),readImage("img/LINELD1.png")},{null,null}},
        {{readImage("img/LINELDL0.png"),readImage("img/LINELDL1.png")},{null,null}}}},
        {{{{readImage("img/LINELU0.png"),readImage("img/LINELU1.png")},{null,null}},
        {{readImage("img/LINELUL0.png"),readImage("img/LINELUL1.png")},{null,null}}},
        {{{readImage("img/LINELUD0.png"),readImage("img/LINELUD1.png")},{null,null}},
        {{readImage("img/LINELUDL0.png"),readImage("img/LINELUDL1.png")},{null,null}}}}},
        {{{{{readImage("img/LINER0.png"),readImage("img/LINER1.png")},
        {readImage("img/LINERR0.png"),readImage("img/LINERR1.png")}},{{null,null},{null,null}}},
        {{{readImage("img/LINERD0.png"),readImage("img/LINERD1.png")},
        {readImage("img/LINERDR0.png"),readImage("img/LINERDR1.png")}},{{null,null},{null,null}}}},
        {{{{readImage("img/LINERU0.png"),readImage("img/LINERU1.png")},
        {readImage("img/LINERUR0.png"),readImage("img/LINERUR1.png")}},{{null,null},{null,null}}},
        {{{readImage("img/LINERUD0.png"),readImage("img/LINERUD1.png")},
        {readImage("img/LINERUDR0.png"),readImage("img/LINERUDR1.png")}},{{null,null},{null,null}}}}}};
        }catch(IOException e){e.printStackTrace();}}
    public BoardFrame(){
        super();
        this.board=new Board();
        board.addModifyListener(new BlockModifyListener());
        addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent ke){
                if(!ke.isControlDown()) return;
                switch(ke.getKeyCode()){
                case'N':{
                    board.resetWithSize(Integer.valueOf(JOptionPane.showInputDialog(BoardFrame.this,"Width:","New Board",JOptionPane.QUESTION_MESSAGE)),
                                        Integer.valueOf(JOptionPane.showInputDialog(BoardFrame.this,"Height:","New Board",JOptionPane.QUESTION_MESSAGE)));
                    repaint();file=null;}break;
                case'S':
                    if(ke.isShiftDown()?!saveAsFile():!saveFile()) JOptionPane.showMessageDialog(BoardFrame.this,"Save failed!");
                    repaint();break;
                case'O':
                    if(!openFile()){
                        JOptionPane.showMessageDialog(BoardFrame.this,"Open failed!");
                        board.clear();
                    }repaint();break;
                case'Q':blockSize+=10;repaint();break;
                case'E':if(blockSize>10){blockSize-=10;repaint();}break;
                case'X':if(!board.isEmpty()){board.clear();repaint();file=null;}break;
                default:{int c=ke.getKeyCode();if('1'<=c&&c<='7')choosedType=c-'1';}break;
        }}});
        addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent me){if(!board.isEmpty()){
                xOfsOrg=xOffset-me.getX();yOfsOrg=yOffset-me.getY();}}
            @Override
            public void mouseClicked(MouseEvent me){if(!board.isEmpty()){
                switch(me.getButton()){
                case MouseEvent.BUTTON1:{
                    Board.Block block=MToBlock(me.getX(),me.getY());
                    if(block!=null) block.clear();}break;
                case MouseEvent.BUTTON3:{
                    Board.Block block=MToBlock(me.getX(),me.getY());if(block!=null)
                    switch(block.getType()){
                    case SRC:block.inverseValue();break;
                    case LINE:block.toNextFacing();break;
                    default:block.setType(Block.Type.valueOf(choosedType));break;}}break;
                default:break;
        }}}});
        addMouseMotionListener(new MouseAdapter(){
            @Override
            public void mouseDragged(MouseEvent me){if(!board.isEmpty()){
                xOffset=me.getX()+xOfsOrg;yOffset=me.getY()+yOfsOrg;repaint();}}});
    }
    @Override
    public void setVisible(boolean visible){
        super.setVisible(visible);
        if(visible) graphics=getGraphics();
    }
    private void paint(Block block,Graphics g){
        int x=block.x*blockSize+xOffset,y=block.y*blockSize+yOffset;
        if(x<-blockSize||getWidth()<=x||y<-blockSize||getHeight()<=y) return;
        Block.Type type=block.getType();
        if(type==Block.Type.LINE){
            int[] o=block.checkOutputs();
            g.drawImage(LINE_IMAGES[block.getFacing()][o[0]][o[1]][o[2]][o[3]][block.getValue()?1:0],x,y,blockSize,blockSize,null);
        }
        else g.drawImage(IMAGES[type.ordinal()][block.getValue()?1:0],x,y,blockSize,blockSize,null);
    }
    private void paint(Block block){paint(block,graphics);}
    @Override
    public void paint(Graphics g){
        if(board==null||board.isEmpty()){g.clearRect(getX(),getY(),getWidth(),getHeight());return;};
        Image image=createImage(getWidth(),getHeight());
        Graphics ig=image.getGraphics();
        int xl=-xOffset/blockSize,yl=-yOffset/blockSize,xr=xl+getWidth()/blockSize,yr=yl+getHeight()/blockSize;
        if(xl<0)xl=0;if(yl<0)yl=0;if(xr>=board.getWidth())xr=board.getWidth()-1;if(yr>=board.getHeight())yr=board.getHeight()-1;
        for(int i=yl;i<=yr;i++)
            for(int j=xl;j<=xr;j++) paint(board.get(j,i),ig);
        g.drawImage(image,0,0,null);
    }
    private Board.Block MToBlock(int x,int y){
        x-=xOffset;y-=yOffset;
        x/=blockSize;y/=blockSize;
        if(x<0||board.getWidth()<=x||y<0||board.getHeight()<=y) return null;
        return board.get(x,y);
    }
    private boolean openFile(){
        JFileChooser fc=new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if(fc.showOpenDialog(fc)!=JFileChooser.APPROVE_OPTION){return false;}
        file=fc.getSelectedFile();
        FileReader reader=null;
        try{reader=new FileReader(file);
        }catch(FileNotFoundException e){e.printStackTrace();return false;}
        if(board.loadFrom(reader)){
            try{reader.close();
            }catch(IOException e){e.printStackTrace();return false;}
            return true;
        }try{reader.close();
        }catch(IOException e){e.printStackTrace();return false;}
        return false;
    }
    private boolean saveFile(){
        if(file==null) return saveAsFile();
        FileWriter writer=null;
        try{writer=new FileWriter(file);
        }catch(IOException e){e.printStackTrace();return false;}
        if(board.exportTo(writer)){
            try{writer.close();
            }catch(IOException e){e.printStackTrace();return false;}
            return true;
        }return false;
    }
    private boolean saveAsFile(){
        JFileChooser fc=new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if(fc.showOpenDialog(fc)!=JFileChooser.APPROVE_OPTION){return false;}
        file=fc.getSelectedFile();
        return saveFile();
    }
    public static void main(String[] args){
        BoardFrame bf=new BoardFrame();
        bf.setTitle("LogicBlocks");
        bf.setSize(1000,600);
        bf.setBackground(Color.WHITE);
        bf.setDefaultCloseOperation(EXIT_ON_CLOSE);
        bf.setVisible(true);
    }
}
