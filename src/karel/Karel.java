/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package karel;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;
import javax.swing.JButton;
import javax.swing.Timer;
import java.lang.*;

/**
 *
 * @author Sam, Amber, Josh, Noel, Heather
 * Github sucks!
 */
public class Karel extends javax.swing.JFrame
{
    private final int OFFSET = 0;
    
    JTextArea lines;
    JTextArea textarea;
    JFrame textframe;
    Thread loop;
    int currSpeed = 5;
    /**
     * Creates new form Karel
     */
    public Karel()
    {
        this.setTitle("Karel");
        initComponents();
        InitUI();
        Timer Gemupdater = new Timer(100, new ActionListener()
        {
          @Override
          public void actionPerformed(ActionEvent evt)
          {
              StepCount.setText("" + world.getStepCount());
              GemCount.setText("" + world.getPlayerGem());
          }
        });
        Gemupdater.start();
        loop = new Thread();
    }
    
    public void InitUI() 
    {
        buttonPanel.setVisible(false);
        manualPanel.setVisible(false);
        blankPanel.setVisible(true);
        // Creating the popout frame with line numbering
        textframe = new JFrame("Programmer Mode");
        // Building Menu
        JMenuBar textbar;
        JMenu textmenu;
        JMenuItem menuRun, menuSave, menuSaveAs;
        textbar = new JMenuBar();
        textmenu = new JMenu("File");
        textmenu.setMnemonic(KeyEvent.VK_A);
        textbar.add(textmenu);
        menuRun = new JMenuItem("Run",
                                  KeyEvent.VK_T);
        menuRun.setAccelerator(KeyStroke.getKeyStroke(
                                    KeyEvent.VK_1, ActionEvent.ALT_MASK));
        textmenu.add(menuRun);

        menuSaveAs = new JMenuItem("Auto Save");
        textmenu.add(menuSaveAs);

        menuSave = new JMenuItem("Save As");
        textmenu.add(menuSave);


        // Creating the JTextArea's
        textframe.setJMenuBar(textbar);
//            textframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JScrollPane textpane = new JScrollPane();
        textarea = new JTextArea();
        lines = new JTextArea("1");
        // Listening for input and adding lines
        textarea.getDocument().addDocumentListener(new DocumentListener()
            {
                    public String getText()
                    {
                            int caretPosition = textarea.getDocument().getLength();
                            Element root = textarea.getDocument().getDefaultRootElement();
                            String text = "1" + System.getProperty("line.separator");
                            for(int i = 2; i < root.getElementIndex( caretPosition ) + 2; i++)
                            {
                                    text += i + System.getProperty("line.separator");
                            }
                            return text;
                    }
                    @Override
                    public void changedUpdate(DocumentEvent de) {
                            lines.setText(getText());
                    }

                    @Override
                    public void insertUpdate(DocumentEvent de) {
                            lines.setText(getText());
                    }

                    @Override
                    public void removeUpdate(DocumentEvent de) {
                            lines.setText(getText());
                    }

            });

        textpane.getViewport().add(textarea);
        textpane.setRowHeaderView(lines);
        textpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        textframe.add(textpane);
        textframe.pack();
        textframe.setSize(390,540);
        textframe.setVisible(false);
        lines.setBackground(Color.LIGHT_GRAY);
        lines.setEditable(false);
        menuRun.addActionListener(new ActionListener() 
        {
               @Override
               public void actionPerformed(java.awt.event.ActionEvent e)
               {
                        textframe.setVisible(false);
                        buttonPanel.setVisible(false);
                        manualPanel.setVisible(true);
                        final List<String> user_input = Arrays.asList(textarea.getText().split("\n"));                       
                        Runnable r1 = new Runnable()
                        {
                             public void run()
                             {
                                  int line_count = world.doScript(0, 0, user_input); // Running
                                  buttonPanel.setVisible(false);
                                  manualPanel.setVisible(false);
                                  textframe.setVisible(false);
                             }
                         };
                         loop = new Thread(r1);
                         loop.start();
               }




            });
        menuSave.addActionListener(new ActionListener() 
            {
               @Override
               public void actionPerformed(java.awt.event.ActionEvent e)
               {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Please Enter File Name and Choose Location");
                    List<String> user_input = Arrays.asList(textarea.getText().split("\n"));
                    PrintWriter out = null;                      

                    int userSelection = fileChooser.showSaveDialog(fileChooser);
                    if (userSelection == JFileChooser.APPROVE_OPTION) 
                    {
                         try 
                         {
                             File fileToSave = fileChooser.getSelectedFile();

                             out = new PrintWriter(fileToSave.getAbsolutePath()+".txt");
                             for(int loop = 0; loop < user_input.size(); loop++)
                             {
                                out.println(user_input.get(loop));                                
                             }

                        out.close();
                         } catch (FileNotFoundException ex) {
                             Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
                         }
                    }

               }                       
            });

        menuSaveAs.addActionListener(new ActionListener() 
            {
               @Override
               public void actionPerformed(java.awt.event.ActionEvent e)
               {
                   try 
                   {
                        List<String> user_input = Arrays.asList(textarea.getText().split("\n"));
                        PrintWriter out;
                        DateFormat dateFormat = new SimpleDateFormat("dd_MMM_HH_mm_ss");
                        Date date = new Date();

                        String fileName1;
                        fileName1 = "KarelCode_";
                        fileName1 += dateFormat.format(date);
                        fileName1 += ".txt";


                        out = new PrintWriter(fileName1);

                        for(int loop = 0; loop < user_input.size(); loop++)
                        {
                           out.println(user_input.get(loop));                                
                        }

                        out.close();
                   } catch (FileNotFoundException ex) {
                       Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
                   }
               }                       
            });      

    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        mainContainer = new javax.swing.JPanel();
        topSubContainer = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        StepCount = new javax.swing.JLabel();
        GemCount = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        bottomSubContainer = new javax.swing.JPanel();
        middleContainer = new javax.swing.JPanel();
        leftContainer = new javax.swing.JPanel();
        buttonPanel = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        manualPanel = new javax.swing.JPanel();
        Slowdown = new javax.swing.JButton();
        Pause = new javax.swing.JButton();
        Speedup = new javax.swing.JButton();
        speedCounter = new javax.swing.JTextField();
        Stop = new javax.swing.JButton();
        Reset = new javax.swing.JButton();
        blankPanel = new javax.swing.JPanel();
        rightContainer = new javax.swing.JPanel();
        world = new karel.World();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1006, 550));
        setPreferredSize(new java.awt.Dimension(990, 546));
        setResizable(false);

        mainContainer.setBackground(new java.awt.Color(51, 0, 0));
        mainContainer.setLayout(new java.awt.BorderLayout());

        topSubContainer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        topSubContainer.setFocusable(false);
        topSubContainer.setMaximumSize(new java.awt.Dimension(32767, 28));
        topSubContainer.setMinimumSize(new java.awt.Dimension(100, 28));
        topSubContainer.setPreferredSize(new java.awt.Dimension(733, 28));

        jButton3.setText("Button Mode");
        jButton3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton3ActionPerformed(evt);
            }
        });

        jButton10.setText("Manual Mode");
        jButton10.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton10ActionPerformed(evt);
            }
        });

        StepCount.setText("0");

        GemCount.setText("0");

        jLabel2.setText("Moves:");

        jLabel3.setText("Gems:");

        javax.swing.GroupLayout topSubContainerLayout = new javax.swing.GroupLayout(topSubContainer);
        topSubContainer.setLayout(topSubContainerLayout);
        topSubContainerLayout.setHorizontalGroup(
            topSubContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topSubContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 635, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(StepCount)
                .addGap(13, 13, 13)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(GemCount)
                .addGap(70, 70, 70))
        );
        topSubContainerLayout.setVerticalGroup(
            topSubContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topSubContainerLayout.createSequentialGroup()
                .addGroup(topSubContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton10)
                    .addComponent(StepCount)
                    .addComponent(GemCount)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(0, 3, Short.MAX_VALUE))
        );

        mainContainer.add(topSubContainer, java.awt.BorderLayout.PAGE_START);

        bottomSubContainer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        bottomSubContainer.setMaximumSize(new java.awt.Dimension(32767, 30));
        bottomSubContainer.setMinimumSize(new java.awt.Dimension(100, 30));

        javax.swing.GroupLayout bottomSubContainerLayout = new javax.swing.GroupLayout(bottomSubContainer);
        bottomSubContainer.setLayout(bottomSubContainerLayout);
        bottomSubContainerLayout.setHorizontalGroup(
            bottomSubContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1019, Short.MAX_VALUE)
        );
        bottomSubContainerLayout.setVerticalGroup(
            bottomSubContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );

        mainContainer.add(bottomSubContainer, java.awt.BorderLayout.PAGE_END);

        middleContainer.setBackground(new java.awt.Color(153, 153, 153));
        middleContainer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        middleContainer.setMaximumSize(new java.awt.Dimension(2147483647, 438));
        middleContainer.setPreferredSize(new java.awt.Dimension(975, 436));
        middleContainer.setLayout(new java.awt.BorderLayout());

        leftContainer.setMaximumSize(new java.awt.Dimension(430, 32767));
        leftContainer.setMinimumSize(new java.awt.Dimension(430, 100));
        leftContainer.setPreferredSize(new java.awt.Dimension(395, 436));
        leftContainer.setLayout(new java.awt.CardLayout());

        buttonPanel.setVisible(false);

        jButton4.setText("Go");
        jButton4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Left");
        jButton5.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Right");
        jButton6.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton6ActionPerformed(evt);
            }
        });

        jButton8.setText("Get");
        jButton8.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText("Put");
        jButton9.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonPanelLayout = new javax.swing.GroupLayout(buttonPanel);
        buttonPanel.setLayout(buttonPanelLayout);
        buttonPanelLayout.setHorizontalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(buttonPanelLayout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(jButton5)
                        .addGap(71, 71, 71)
                        .addComponent(jButton6))
                    .addGroup(buttonPanelLayout.createSequentialGroup()
                        .addGap(129, 129, 129)
                        .addComponent(jButton4))
                    .addGroup(buttonPanelLayout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(jButton8)
                        .addGap(41, 41, 41)
                        .addComponent(jButton9)))
                .addContainerGap(149, Short.MAX_VALUE))
        );

        buttonPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton4, jButton5, jButton6, jButton8, jButton9});

        buttonPanelLayout.setVerticalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jButton6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton8)
                    .addComponent(jButton9))
                .addContainerGap(321, Short.MAX_VALUE))
        );

        leftContainer.add(buttonPanel, "card2");

        Slowdown.setText("Slowdown");
        Slowdown.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                Slowdown(evt);
            }
        });

        Pause.setText("Pause");
        Pause.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                Pause(evt);
            }
        });

        Speedup.setText("Speedup");
        Speedup.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                Speedup(evt);
            }
        });

        speedCounter.setBackground(new java.awt.Color(204, 204, 204));
        speedCounter.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        speedCounter.setText("Speed:        " + currSpeed);

        Stop.setText("Stop");
        Stop.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                Stop(evt);
            }
        });

        Reset.setText("Reset");
        Reset.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                Reset(evt);
            }
        });

        javax.swing.GroupLayout manualPanelLayout = new javax.swing.GroupLayout(manualPanel);
        manualPanel.setLayout(manualPanelLayout);
        manualPanelLayout.setHorizontalGroup(
            manualPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, manualPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(speedCounter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(130, 130, 130))
            .addGroup(manualPanelLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(manualPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(manualPanelLayout.createSequentialGroup()
                        .addComponent(Slowdown)
                        .addGap(49, 49, 49))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, manualPanelLayout.createSequentialGroup()
                        .addComponent(Stop, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)))
                .addGroup(manualPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(manualPanelLayout.createSequentialGroup()
                        .addComponent(Pause)
                        .addGap(56, 56, 56)
                        .addComponent(Speedup)
                        .addContainerGap(26, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, manualPanelLayout.createSequentialGroup()
                        .addComponent(Reset, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(78, 78, 78))))
        );
        manualPanelLayout.setVerticalGroup(
            manualPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(manualPanelLayout.createSequentialGroup()
                .addGap(96, 96, 96)
                .addComponent(speedCounter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(manualPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Slowdown)
                    .addComponent(Pause)
                    .addComponent(Speedup))
                .addGap(37, 37, 37)
                .addGroup(manualPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Stop, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Reset, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(188, Short.MAX_VALUE))
        );

        leftContainer.add(manualPanel, "card3");

        javax.swing.GroupLayout blankPanelLayout = new javax.swing.GroupLayout(blankPanel);
        blankPanel.setLayout(blankPanelLayout);
        blankPanelLayout.setHorizontalGroup(
            blankPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 395, Short.MAX_VALUE)
        );
        blankPanelLayout.setVerticalGroup(
            blankPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 465, Short.MAX_VALUE)
        );

        leftContainer.add(blankPanel, "card4");

        middleContainer.add(leftContainer, java.awt.BorderLayout.LINE_START);

        rightContainer.setMinimumSize(new java.awt.Dimension(589, 100));
        rightContainer.setPreferredSize(new java.awt.Dimension(589, 469));
        rightContainer.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout worldLayout = new javax.swing.GroupLayout(world);
        world.setLayout(worldLayout);
        worldLayout.setHorizontalGroup(
            worldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 624, Short.MAX_VALUE)
        );
        worldLayout.setVerticalGroup(
            worldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 465, Short.MAX_VALUE)
        );

        rightContainer.add(world);

        middleContainer.add(rightContainer, java.awt.BorderLayout.CENTER);

        mainContainer.add(middleContainer, java.awt.BorderLayout.CENTER);

        jMenu1.setText("File");

        jMenuItem2.setText("Open New Map From File");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem1.setText("Reset Current Map");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Themes");

        jMenuItem5.setText("MegaMan");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuBar1.add(jMenu3);

        jMenu2.setText("Help");

        jMenuItem3.setText("Open Help File (.txt)");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuItem4.setText("Open Help File (.html)");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton3ActionPerformed
    {//GEN-HEADEREND:event_jButton3ActionPerformed
        manualPanel.setVisible(false);
        textframe.setVisible(false);
        loop.stop();
        buttonPanel.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton4ActionPerformed
    {//GEN-HEADEREND:event_jButton4ActionPerformed
        world.choiceMade("go");
        
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton5ActionPerformed
    {//GEN-HEADEREND:event_jButton5ActionPerformed
        world.choiceMade("left");
        
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton6ActionPerformed
    {//GEN-HEADEREND:event_jButton6ActionPerformed
        world.choiceMade("right");
        
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton8ActionPerformed
    {//GEN-HEADEREND:event_jButton8ActionPerformed
        world.choiceMade("get");
//        GemCount.setText("" + world.getPlayerGem());
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton9ActionPerformed
    {//GEN-HEADEREND:event_jButton9ActionPerformed
        world.choiceMade("put");
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton10ActionPerformed
    {//GEN-HEADEREND:event_jButton10ActionPerformed
        buttonPanel.setVisible(false);
        manualPanel.setVisible(false);
        loop.stop();
        textframe.setVisible(true);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
    //Open help file (write help file...)
        Desktop dt = Desktop.getDesktop();
        try
        {
            dt.open( new File("help.txt") );
        } catch (IOException e) {//exception handling?
            }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        // get a file path from the user
        loop.stop();
        buttonPanel.setVisible(false);
        manualPanel.setVisible(false);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Please Specify the File To Open");
        File fileToOpen;
        BufferedReader readIn;
        String newMap = new String();
        
        int userSelection = userSelection = fileChooser.showOpenDialog(fileChooser);

        if (userSelection == JFileChooser.APPROVE_OPTION) 
        {
            try {
                fileToOpen = fileChooser.getSelectedFile();
                readIn = new BufferedReader(new FileReader(fileToOpen));
                
                while(readIn.ready())
                {
                    newMap += readIn.readLine();
                    newMap += '\n';
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Karel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Karel.class.getName()).log(Level.SEVERE, null, ex);
            }            
            //file is now in newMap string, turn string into actual new map!
            world.setLevelString(newMap);
            world.worldDeleter();
            world.initWorld();

            //paint
            this.repaint();
            
        }
        
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        loop.stop();
        buttonPanel.setVisible(false);
        manualPanel.setVisible(false);
        world.worldDeleter();
        world.initWorld();

        //paint
        this.repaint();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void Pause(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Pause
        Object source = evt.getSource();
        JButton switcher;
        switcher = (JButton) source;
        if (switcher.getText().equals("Pause"))
        {
            loop.suspend();
            switcher.setText("Resume");
        }
        else if (switcher.getText().equals("Resume"))
        {
            loop.resume();
            switcher.setText("Pause");
        }
    }//GEN-LAST:event_Pause

    private void Slowdown(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Slowdown
        if (currSpeed > 1)
        {
            --currSpeed;
            world.setSpeed(currSpeed);
            speedCounter.setText("Speed:        " + currSpeed);
        }
    }//GEN-LAST:event_Slowdown

    private void Speedup(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Speedup
        if (currSpeed < 10)
        {
            ++currSpeed;
            world.setSpeed(currSpeed);
            speedCounter.setText("Speed:        " + currSpeed);
        }
    }//GEN-LAST:event_Speedup

    private void Reset(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Reset
        loop.stop();
        world.worldDeleter();
        world.initWorld();
        buttonPanel.setVisible(false);
        manualPanel.setVisible(false);
        //paint
        this.repaint();
    }//GEN-LAST:event_Reset

    private void Stop(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Stop
        loop.stop();
        buttonPanel.setVisible(false);
        manualPanel.setVisible(false);
    }//GEN-LAST:event_Stop

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        Desktop dt = Desktop.getDesktop();
        try
        {
            dt.open( new File("help.html") );
        } catch (IOException e) {//exception handling?
            }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        //set a MegaMan Theme!
        world.setThemes("Mega");
        this.repaint();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(Karel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(Karel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(Karel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(Karel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                Karel karel = new Karel();
                karel.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel GemCount;
    private javax.swing.JButton Pause;
    private javax.swing.JButton Reset;
    private javax.swing.JButton Slowdown;
    private javax.swing.JButton Speedup;
    private javax.swing.JLabel StepCount;
    private javax.swing.JButton Stop;
    private javax.swing.JPanel blankPanel;
    private javax.swing.JPanel bottomSubContainer;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JPanel leftContainer;
    private javax.swing.JPanel mainContainer;
    private javax.swing.JPanel manualPanel;
    private javax.swing.JPanel middleContainer;
    private javax.swing.JPanel rightContainer;
    private javax.swing.JTextField speedCounter;
    private javax.swing.JPanel topSubContainer;
    private karel.World world;
    // End of variables declaration//GEN-END:variables
}
