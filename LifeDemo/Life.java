import java.awt.*;
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import javax.swing.event.*;  // Needed for ActionListener
import java.io.*;
import java.io.File;
import java.util.Scanner;

class LifeSimulationGUI extends JFrame implements ActionListener, ChangeListener
{
    static Colony colony = new Colony (0.1);//creates colony object
    static JSlider speedSldr = new JSlider ();//slider for speed
    static Timer t;//timer
    static JComboBox <Integer> xnum = new JComboBox();//declares JCOmboBox
    static JComboBox<Integer> ynum = new JComboBox();//declares JCOmboBox
    static JComboBox<Integer> snum = new JComboBox();//declares JCOmboBox
    static JLabel xfield = new JLabel ();//declares JLabel
    static JLabel yfield = new JLabel ();//declares JLabel
    static JLabel sfield = new JLabel ();//declares JLabel
    static JTextField ftxt = new JTextField (15);//variable declaration of text field
    //======================================================== constructor
    public LifeSimulationGUI ()
    {
        // 1... Create/initialize components
        JButton simulateBtn = new JButton ("Simulate");//creates button
        simulateBtn.addActionListener (this);//adds listener
        
        JButton popBtn = new JButton ("Populate");//creates button
        popBtn.addActionListener (this);//adds listener
        
        JButton eraBtn = new JButton ("Eradicate");//creates button
        eraBtn.addActionListener (this);//adds listener
        
        JButton saBtn = new JButton ("Save");//creates button
        saBtn.addActionListener (this);//adds listener
        
        JButton loBtn = new JButton ("Load");//creates button
        loBtn.addActionListener (this);//adds listener
        
        speedSldr.addChangeListener (this);//adds listener
        
        xfield.setText("x-coordinate");//sets text for label
        yfield.setText("y-coordinate");//sets text for label
        sfield.setText("size");//sets text for label
        
        
        // 2... Create content pane, set layout
        JPanel content = new JPanel ();        // Create a content pane
        content.setLayout (new BorderLayout ()); // Use BorderLayout for panel
        JPanel north = new JPanel ();//declares pane;
        north.setLayout (new FlowLayout ()); // Use FlowLayout for input area
        JPanel south = new JPanel ();//declares pane;
        south.setLayout (new FlowLayout ()); // Use FlowLayout for input area
        
        DrawArea board = new DrawArea (500, 500);//creates board with specified size
        
        xnum=new JComboBox<Integer>();//initializes JComboBox
        for (int xn=0; xn<=100; xn++)//for loop which runs through card values
        {
           xnum.addItem(xn); //adds value to JComboBox
        }
        
        ynum=new JComboBox<Integer>();//initializes JComboBox
        for (int yn=0; yn<=100; yn++)//for loop which runs through card values
        {
           ynum.addItem(yn); //adds value to JComboBox
        }
        
        snum=new JComboBox<Integer>();//initializes JComboBox
        for (int sn=1; sn<=100; sn++)//for loop which runs through card values
        {
           snum.addItem(sn); //adds value to JComboBox
        }
        
        // 3... Add the components to the input area.

        north.add (simulateBtn);//adds componenet to north panel
        north.add (speedSldr);//adds componenet to north panel
        north.add(xfield);//adds componenet to north panel
        north.add(xnum);//adds componenet to north panel
        north.add(yfield);//adds componenet to north panel
        north.add(ynum);//adds componenet to north panel
        north.add(sfield);//adds componenet to north panel
        north.add(snum);//adds componenet to north panel
        north.add(popBtn);//adds componenet to north panel
        north.add(eraBtn);//adds componenet to north panel
        south.add (ftxt);//adds componenet to north panel
        south.add(saBtn);//adds componenet to north panel
        south.add(loBtn);//adds componenet to north panel

        content.add (north, "North"); // Input area
        content.add (south, "Center"); // Input area
        content.add (board, "South"); // Output area

        // 4... Set this window's attributes.
        setContentPane (content);//sets pane
        pack ();//formatting
        setTitle ("Life Simulation");//sets title
        //setSize (510, 570);
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);//end behaviour
        setLocationRelativeTo (null);           // Center window.
    }

    public void stateChanged (ChangeEvent e)//changes state of speed
    {
        if (t != null)
            t.setDelay (400 - 4 * speedSldr.getValue ()); // 0 to 400 ms
    }

    public void actionPerformed (ActionEvent e)
    {
      Movement moveColony = new Movement (colony); // ActionListener
      
        if (e.getActionCommand ().equals ("Simulate"))
        {
            t = new Timer (200, moveColony); // set up timer
            t.start (); // start simulation
        }
        
        else if (e.getActionCommand ().equals ("Populate"))
        { 
         colony.populate(xnum.getSelectedIndex() , ynum.getSelectedIndex() , snum.getSelectedIndex() );//calls populate method with info from JComboBoxes  
        }
        
        else if (e.getActionCommand ().equals ("Eradicate"))
        {
          colony.eradicate(xnum.getSelectedIndex() , ynum.getSelectedIndex() , snum.getSelectedIndex() );   //calls eradicate method with info from JComboBoxes  
        }
        
        else if (e.getActionCommand ().equals ("Save"))
        {
          colony.save(ftxt.getText());  //calls save method with info from text field
        }
        
        else if (e.getActionCommand ().equals ("Load"))
         {
          colony.load(ftxt.getText());//calls load method with info from text field
          }
                   
        repaint ();            // refresh display of deck
    }

    class DrawArea extends JPanel
    {
        public DrawArea (int width, int height)
        {
            this.setPreferredSize (new Dimension (width, height)); // size
        }

        public void paintComponent (Graphics g)
        {
            colony.show (g);//calls show method to display array in a graphic
        }
    }

    class Movement implements ActionListener
    {
        private Colony colony;//variable declaation

        public Movement (Colony col)
        {
            colony = col;//copies object
        }

        public void actionPerformed (ActionEvent event)
        {
            colony.advance ();//calls advance method to further smooth array
            repaint ();//repaints to refresh graphic
        }
    }

}

class Colony
{
    private boolean grid[] [];//array declaration
    
    public Colony (double density)
    {
        grid = new boolean [100] [100];//declares 2-D array with size 100
        for (int row = 0 ; row < grid.length ; row++)//for loop which runs through rows
            for (int col = 0 ; col < grid [0].length ; col++)//for loop which runs through columns
                grid [row] [col] = Math.random () < density;//fills array with randomized boolean variables
    }
    
    public void load (String f)
    {String textt="";//string declaration and initialization
       String path="";
        try{
        File F=new File (f);//declares file
     path = F.getAbsolutePath();//gets where text is being searched for
     Scanner sc=new Scanner(F); //declares scanner
        int row=0;//varaible declaration
        int size=0;
        int counter=0;
        while (sc.hasNextLine())//runs through all of files lines
        {
            sc.nextLine();//goes to next line
            counter++;//adds to counter
        }
        sc.close();//closes scanner
        sc=new Scanner(F);//declares scanner again (start on top)
         grid = new boolean[counter][];//declares 2-d array
        while (sc.hasNextLine())//while lines still exist
        {
            textt=sc.nextLine();//reads line
            String [] inp=textt.split(",");//splits values by commas and puts them in string array
            if (grid[row]==null)//if the column is eptmy
            {
                size=inp.length;//declares size for the 2-d array
                grid[row]=new boolean [size];//declares new array
            }
            for (int ccol=0; ccol<inp.length; ccol++)//for loop which runs through columns 1-d array
            {
                grid[row][ccol]=Integer.parseInt(inp[ccol])==1;//uses parse int to convert the string to ints and adds them to 2-d array
                
            }
            row++;//increases row
        }
        }
        catch (Exception e3)//catches error with no file
                {System.out.println("No file found,cannot load, ensure it is saved in "+path);//error message
        }
    }
    

    public void show (Graphics g)
    {
        for (int row = 0 ; row < grid.length ; row++)//for loop which runs through rows
            for (int col = 0 ; col < grid [0].length ; col++)//for loop which runs through columns
            {
                if (grid [row] [col]) // if there is life
                    g.setColor (Color.black);//set color to black
                else//otherwise
                    g.setColor (Color.white);//set color to white
                g.fillRect (col * 5 + 2, row * 5 + 2, 5, 5); // draw life form
        }
    }

    public boolean live (int row, int col)
   {
      int counter=0;//initializes temp variable to count number of life forms around
      
        if (col!=0) 
        {if (grid[row  ][col-1]==true)   {counter++;}}//if there is a value to the left (not in the left most column), increase counter
        if (col!=grid[row].length-1) 
        {if (grid[row  ][col+1]==true)    {counter++;}}//if there is a value to the right (not in the right most column), and increase counter
        
        if (row!=0) 
        {
        if (grid[row-1][col  ]==true)   {counter++;}//if there is a value directly above (not in the top most row), increase counter
        if ( col!=0) 
        {if (grid[row-1][col-1])  {counter++;}}//if there is a value diretly above and to the left (not in the left most column or top most row),  increase counter
        if (col!=grid[row].length-1) 
        {if (grid[row-1][col+1])   {counter++;}}//if there is a value diretly above and to the right (not in the right most column or top most row), increase counter
        }
        
        if (row!=grid.length-1) 
        {if (grid[row+1][col])   {counter++;}//if there is a value directly below (not in the bottom most row),increase counter
        if ( col!=0) 
        {if (grid[row+1][col-1])   {counter++;}}//if there is a value diretly below and to the left (not in the left most column or top most row),increase counter
        if (col!=grid[row].length-1) 
        {if (grid[row+1][col+1])   {counter++;}}//if there is a value diretly below and to the right (not in the right most column or top most row),increase counter
        } 
        
        if (counter==2 && grid[row][col]==true)//if there are two life forms around and the life form is alive
        {return true;}//keep it living
        else if (counter==3)//if there are three life forms around
        {return true;}//keep it living
        else {return false;}//otherwise render it dead
      }
    
    

    public void advance ()
    {
        boolean nextGen[] [] = new boolean [grid.length] [grid [0].length]; // create next generation of life forms
        for (int rowl = 0 ; rowl < grid.length ; rowl++)//for loop which runs through rows
            for (int coll = 0 ; coll < grid [0].length ; coll++)//for loop which runs through columns
                nextGen [rowl] [coll] = live (rowl, coll); // determine life/death status
        grid = nextGen; // update life forms
    }
    
    public void populate (int x, int y, int s)
    {
      
      for (int r=y; r<=s+y&&r<100; r++)//for loop which runs through rows
      {for (int c=x; c<=s+x&&c<100; c++)//for loop which runs through columns
        {
        int rand=(int)Math.random()*100+1;//generates random number between 1-100
        if (rand<90)//if number is less than 90, for probability
        {grid[r][c]=true;}//make life form living
      }}
    
      
    }
    
    public void eradicate (int xc, int yc, int sc)//method to eradicate
    {
        
      for (int ro=yc; ro<=sc+yc&&ro<100; ro++)//for loop which runs through rows
      {for (int co=xc; co<=sc+xc&&co<100; co++)//for loop which runs through columns
        {
        int rando=(int)Math.random()*100+1;//generates random number between 1-100
        if (rando<90)//if number is less than 90, for probability
        {grid[ro][co]=false;}//make life form dead
      }}
        
    }
    
    public void save (String filename) {
      String spath="";//variable declaration
      File sf=new File(filename);
      spath=sf.getAbsolutePath();
        try{
      BufferedWriter outputWriter = null;//declares BufferedWriter
      outputWriter = new BufferedWriter(new FileWriter(filename));//uses file to initialize BufferedWriter
      
      for (int rl = 0; rl < grid.length; rl++) {//for loop which runs through rows
        for (int cl = 0; cl < grid[rl].length; cl++) {//for loop which runs through columns
          if (cl!=grid[rl].length-1)//if value is not last
          {if (grid[rl][cl]==true)//if cell is live
          {outputWriter.write("1,");}//print  1 with comma
          else //otherwise
          {outputWriter.write("0,");}}//print 0 with comma
          
          else//else
          {if (grid[rl][cl]==true)//if cell is live
          {outputWriter.write("1");}//print  1
          else //otherwise
          {outputWriter.write("0");}}//print 0
                           }
        outputWriter.newLine();//goes to next line
      }
  outputWriter.flush();  //flushes outputWriter
  outputWriter.close(); }//closes outputWriter
      catch(IOException ie) {System.out.println("No file found,cannot save, ensure it is saved in "+spath);//error message

}

}
}
