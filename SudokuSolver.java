/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudokusolver;

import java.io.*;
//import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author pradeep kumar
 */
class SudokuSolver implements ActionListener {
    
    
    static JFrame f;
    //JButton[][] sudoku = new JButton[9][9];
    JPanel cont,p,p1,p2;
    static JButton b,b1;
    static int x,y,size = 9 ;
    static JTextField[] tf = new JTextField[size*size];
    static JRadioButton rb1 , rb2 ;
    
    static int[][] sudoku = new int[size][size] ;
    static double time = 0.00 ;
    static boolean flag = false ;
    
    public SudokuSolver()
    {
        f = new JFrame("Sudoku Solving Java Application");
        
        cont = new JPanel();
        cont.setLayout(new BoxLayout(cont,BoxLayout.Y_AXIS));
        /*
        p2 = new JPanel();
        
        
        rb1 = new JRadioButton("sudoku 9X9");
        
        rb2 = new JRadioButton("sudoku 16x16",true);
        
        ButtonGroup bg = new ButtonGroup();
        
        bg.add(rb1);
        bg.add(rb2);
        
        p2.add(rb1);
        p2.add(rb2);
        
        cont.add(p2);*/
        
        
        p = new JPanel(new GridLayout(size,size));           //has fixed size
        
        
        for(int i = 0 ; i < size ; i++ )
        {
            for(int j = 0 ; j < size ; j++ )
            {
                //sudoku[i][j] = new JButton("0");
                //(sudoku[i][j]).addActionListener(this);
                tf[i*size + j] = new JTextField("");
                p.add(tf[i*size + j]);
            }
        }
        
        coloringGrid();               //color boxes differently.
        
       // p = new JPanel(new GridBagLayout());                   //has dynamic sizes
        
        
        
        p1 = new JPanel();
        
        b = new JButton("Solve Sudoku");
        b.addActionListener(this);
        
        p1.add(b);
        
        b1 = new JButton("Reset Sudoku");
        b1.addActionListener(this);
        
        p1.add(b1);
        
        cont.add(p);
        cont.add(p1);
        //cont.add(b1);
        
        f.add(cont);
        
        f.pack();
        f.setVisible(true);
        //f.setSize(500,410);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    /*DocumentListener dl = new DocumentListener()
    {
        public void update(DocumentEvent de)
        {
            
        }
    } ;
    */
    
    
    
    public static void coloringGrid()
    {
        for(int i=0 ; i<(int)Math.sqrt(size); i++ )
        {
            for(int j=0 ; j<(int)Math.sqrt(size) ; j++ )
            {
               if( (i+j) % 2 != 0 )
               {
                   int r = i*((int)Math.sqrt(size)) ;
                   int c = j*((int)Math.sqrt(size)) ;
                   for(int x = 0 ; x < (int)Math.sqrt(size) ; x++)
                   {
                       for(int y = 0 ; y < (int)Math.sqrt(size) ; y++)
                       {
                           tf[(r+x)*size + (c+y)].setBackground(Color.LIGHT_GRAY);
                       }
                   }
                   
               }
            }
        }
    }
    
    
    
    
    
    public void actionPerformed(ActionEvent e)
    {/*
        if( rb1.isSelected() )
        {
            size = 9;
        }
        if( rb2.isSelected() )
        {
            //size = 16;
            SwingWorker<String,String> sw = new SwingWorker<String,String>()
            {
                @Override
                protected String doInBackground() throws Exception
                {
                    size = 16;
                    //p.revalidate();
                    //p.repaint();
                    //cont.repaint();
                    f.repaint();
                    return "";
                }
            };
        }
        */
        if( e.getSource() == b)
        {
            //using swingWorker for backgrounding thread
            //this prevents GUI from lockup.
            
            /**************              ****              ****************/
            
            SwingWorker<String,String> sw = new SwingWorker<String, String>()
            {
                @Override
                protected String doInBackground() throws Exception
                {
                    solve();
                    return "";
                }
            } ;
            
            sw.execute();             //execute
            
            //solve();
            //JOptionPane.showMessageDialog(f, "Solved.");
            if( flag )
                b.setText("Solved! Time : "+time+" Seconds.");
            else
                b.setText("Solve Sudoku");
        }
        
        if( e.getSource() == b1 )
        {
            reset();
        }
    }
    
    
    
  
    
    private static void reset()
    {
        for(int i = 0 ; i < size*size ; i++ )
        {
            //tf[i].setText("");
            try{
                tf[i].setText("");
                //Thread.sleep(10);
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(f,e);
            }
        }
        
        b.setText("Solve Sudoku");
        
        JOptionPane.showMessageDialog(f, "Sudoku Successfully Reset.");
    }
    
    
    
    
    
    
    public static void main(String[] args) throws IOException {
        new SudokuSolver();
    }
    
    
    
    
    
    
    private static void solve()
    {
        long t1 = System.currentTimeMillis();
        
        makeSudoku();
        
        if( validate() )
        {
            if( solveSudoku() )
            { 
                flag = true ;
                //JOptionPane.showMessageDialog(f,"Solved !");
            }
            else
            {
                JOptionPane.showMessageDialog(f,"Invalid sudoku ! Please try Again. Time : "+time+" Sec.");
            }
        }
        else
        {
            JOptionPane.showMessageDialog(f,"Invalid sudoku ! Please try Again\nMaybe some value not between 1 and "+size);
        }
        
        time = ( System.currentTimeMillis() - t1 )/1000.000 ;
    }
    
    
    
    
    
    private static void makeSudoku()
    {
        for(int i=0 ; i< size ; i++ )
        {
            for(int j=0 ; j< size ; j++ )
            {
                if( ( (tf[i*size + j]).getText() ).equals("") )
                    sudoku[i][j] = 0 ;
                else
                    sudoku[i][j] = Integer.parseInt((tf[i*size + j]).getText());
                    //sudoku[i][j] = (int)( (tf[i*size + j]).getText().charAt(0) ) - 48;
            }
        }
    }
    
    
    
    
    
    
    private static boolean solveSudoku()
    {
        int row = 0 , col = 0;
        boolean flag1 = false;
        //find unassigned location
        for( row = 0 ; row < size ; row++ )
        {
            for( col = 0 ; col < size ; col++ )
            {
                if( sudoku[row][col] == 0 )
                {
                    flag1 = true;
                    break;
                }
            }
            if( flag1 == true )
                break;
        }
        
        if( flag1 == false )     //if no unassigned
            return true ;
        
        for(int n = 1 ; n <= size ; n++ )
        {
            if( isSafe(row,col,n) )
            {
                //make assignment
                sudoku[row][col] = n ;
                //print output
                //(tf[(row)*size + col]) = new JTextField(Integer.toString(n));
                (tf[(row)*size + col]).setText(Integer.toString(n));
                //(tf[(row)*size + col]).revalidate();
                //(tf[(row)*size + col]).setVisible(true);
                //(tf[(row)*size + col]).repaint();
                //f.setVisible(true);
                try
                {
                    //Thread.sleep(100);     // milli-second
                }
                catch(Exception e)
                {
                    JOptionPane.showMessageDialog(f,e);
                }
                //return if success
                if( solveSudoku() )
                    return true;
                //if fail , undo and try again
                sudoku[row][col] = 0 ;
            }
        }
        
        //trigger backtracking
        return false ;
    }
    
    
    
    
    
    private static boolean validate()
    {
        for(int i=0 ; i<size ; i++ )
        {
            for(int j=0 ; j<size ; j++ )
            {
                if( sudoku[i][j] < 0 || sudoku[i][j] > size )
                {
                    
                    //JOptionPane.showMessageDialog(f,"Input should be 1 to 16 only . problem in ("+ (i+1)+","+(j+1)+")") ;
                    return false ;
                }
                
                if( sudoku[i][j] != 0 && (usedInRow(i,j,sudoku[i][j]) || usedInCol(i,j,sudoku[i][j]) || usedInBox(i,j, sudoku[i][j]) ) )
                {
                    return false ;
                }
            }
        }
        
        return true ;
    }
    
    
    
    
    private static boolean isSafe(int r , int c , int n)
    {
        return ( !usedInRow(r,c,n) && !usedInCol(r,c,n) && !usedInBox(r,c,n) ) ;
    }
    
    
    
    
    private static boolean usedInRow(int r , int c, int n)
    { 
        for(int col=0 ; col<size ; col++ )
        {
            if( col != c && sudoku[r][col] == n )
            {
                //JOptionPane.showMessageDialog(f,"used in row: ("+(r+1)+","+(c+1)+") ");
                return true;
            }
        }
        
        
        return false;
    }
    
    
    
    
    private static boolean usedInCol(int r,int c , int n)
    {
        for(int row=0 ; row < size ; row++ )
        {
            if( row != r && sudoku[row][c] == n )
            {
                //JOptionPane.showMessageDialog(f,"used in column : ("+(r+1)+","+(c+1)+") ");
                return true;
            }
        }
        
        return false;
    }
    
    
    
    
    
    private static boolean usedInBox(int r , int c , int n)
    {
        int r_st = r - r%((int)Math.sqrt(size)) ;
        int c_st = c - c%((int)Math.sqrt(size)) ;
        
        for(int i=0 ; i< (int)Math.sqrt(size) ; i++ )
        {
            for(int j=0 ; j< (int)Math.sqrt(size) ; j++ )
            {
                if( r_st+i != r && c_st+j != c && sudoku[r_st+i][c_st+j] == n )
                {
                    //JOptionPane.showMessageDialog(f,"used in box : ("+(r+1)+","+(c+1)+") =>"+(r_st)+" , "+ (c_st) +" ij"+i+","+j );
                    return true;
                }
            }
        }
        return false;
    }
               
}