/*      */ package HTMLEditor;
/*      */
/*      */ import java.awt.Color;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.FlowLayout;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.io.File;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.FileWriter;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import javax.swing.AbstractAction;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.ButtonGroup;
/*      */ import javax.swing.ImageIcon;
/*      */ import javax.swing.JButton;
/*      */ import javax.swing.JDialog;
/*      */ import javax.swing.JFileChooser;
/*      */ import javax.swing.JMenu;
/*      */ import javax.swing.JMenuBar;
/*      */ import javax.swing.JMenuItem;
/*      */ import javax.swing.JOptionPane;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JScrollPane;
/*      */ import javax.swing.JTextField;
/*      */ import javax.swing.JTextPane;
/*      */ import javax.swing.JToolBar;
/*      */ import javax.swing.text.Document;
/*      */ import javax.swing.text.MutableAttributeSet;
/*      */ import javax.swing.text.SimpleAttributeSet;
/*      */ import javax.swing.text.StyleConstants;
/*      */ import javax.swing.text.StyledEditorKit;
/*      */ import javax.swing.text.StyledEditorKit.AlignmentAction;
/*      */ import javax.swing.text.StyledEditorKit.ForegroundAction;
/*      */ import javax.swing.text.StyledEditorKit.StyledTextAction;
/*      */ import javax.swing.text.html.HTMLDocument;
/*      */ import javax.swing.text.html.HTMLEditorKit;
/*      */ import javax.swing.undo.UndoManager;
/*      */
/*      */ public class HTMLEditor extends javax.swing.JFrame implements ActionListener
/*      */ {
/*      */   private HTMLDocument document;
/*   46 */   private JTextPane textPane = new JTextPane();
/*   47 */   private JTextPane textPane2 = new JTextPane();
/*   48 */   private boolean debug = false;
/*      */
/*      */   private File currentFile;
/*      */
/*   52 */   protected javax.swing.event.UndoableEditListener undoHandler = new UndoHandler();
/*      */
/*      */
/*   55 */   protected UndoManager undo = new UndoManager();
/*      */
/*   57 */   private UndoAction undoAction = new UndoAction();
/*   58 */   private RedoAction redoAction = new RedoAction();
/*   59 */   private Action cutAction = new javax.swing.text.DefaultEditorKit.CutAction();
/*   60 */   private Action copyAction = new javax.swing.text.DefaultEditorKit.CopyAction();
/*   61 */   private Action pasteAction = new javax.swing.text.DefaultEditorKit.PasteAction();
/*      */
/*   63 */   private Action boldAction = new javax.swing.text.StyledEditorKit.BoldAction();
/*   64 */   private Action underlineAction = new javax.swing.text.StyledEditorKit.UnderlineAction();
/*   65 */   private Action italicAction = new javax.swing.text.StyledEditorKit.ItalicAction();
/*      */
/*   67 */   private Action insertBreakAction = new javax.swing.text.DefaultEditorKit.InsertBreakAction();
/*      */
/*   69 */   private javax.swing.text.html.HTMLEditorKit.InsertHTMLTextAction unorderedListAction = new javax.swing.text.html.HTMLEditorKit.InsertHTMLTextAction("Unordered List", "<ul><li> </li></ul>", javax.swing.text.html.HTML.Tag.P, javax.swing.text.html.HTML.Tag.UL);
/*      */
/*      */
/*      */
/*   73 */   private javax.swing.text.html.HTMLEditorKit.InsertHTMLTextAction bulletAction = new javax.swing.text.html.HTMLEditorKit.InsertHTMLTextAction("Unordered Bullets", "<li> </li>", javax.swing.text.html.HTML.Tag.UL, javax.swing.text.html.HTML.Tag.LI);
/*      */
/*   75 */   public HTMLEditor() { super("HTMLEditor");
/*   76 */     HTMLEditorKit editorKit = new HTMLEditorKit();
/*   77 */     this.document = ((HTMLDocument)editorKit.createDefaultDocument());
/*      */     try
/*      */     {
/*   80 */       javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getCrossPlatformLookAndFeelClassName());
/*   81 */       javax.swing.UIManager.getLookAndFeelDefaults().put("textPane2.caretForeground", Color.white);
/*      */
/*      */     }
/*      */     catch (Exception exc)
/*      */     {
/*   86 */       System.err.println("Error loading L&F: " + exc);
/*      */     }
/*   88 */     init();
/*      */   }
/*      */
/*      */   public static void main(String[] args)
/*      */   {
/*   93 */     HTMLEditor editor = new HTMLEditor();
/*   94 */     editor.setResizable(false);
/*      */   }
/*      */
/*      */   public void init()
/*      */   {
/*   99 */     addWindowListener(new java.awt.event.WindowAdapter() {
/*      */       public void windowClosing(java.awt.event.WindowEvent e) {
/*  101 */         System.exit(HTMLEditor.this.getDefaultCloseOperation());
/*      */       }
/*      */
/*      */
/*  105 */     });
/*  106 */     this.textPane.addMouseListener(new java.awt.event.MouseAdapter()
/*      */     {
/*      */       public void mouseClicked(MouseEvent me)
/*      */       {
/*  110 */         int x = me.getX();
/*  111 */         int y = me.getY();
/*  112 */         int startOffset = HTMLEditor.this.textPane.viewToModel(new java.awt.Point(x, y));
/*  113 */         String text = HTMLEditor.this.textPane.getText();
/*  114 */         int searchLocation = text.indexOf("student://", startOffset);
/*      */
/*      */       }
/*      */
/*      */
/*      */
/*  120 */     });
/*  121 */     JToolBar toolbar = new JToolBar();
/*  122 */     JToolBar toolbar2 = new JToolBar();
/*  123 */     JPanel buttonPane = new JPanel(new FlowLayout(1));
/*  124 */     buttonPane.setOpaque(false);
/*  125 */     buttonPane.add(toolbar2);
/*  126 */     toolbar.setRollover(false);
/*  127 */     toolbar.setFloatable(false);
/*  128 */     toolbar.setAlignmentX(0.0F);
/*  129 */     toolbar2.setRollover(false);
/*  130 */     toolbar2.setFloatable(false);
/*  131 */     toolbar2.setAlignmentX(0.0F);
/*      */
/*  133 */     JButton button = new JButton(new ImageIcon("images/newfile (Custom).png"));
/*  134 */     toolbar.add(button);
/*  135 */     toolbar.addSeparator();
/*  136 */     JButton button2 = new JButton(new ImageIcon("images/open (Custom).png"));
/*  137 */     toolbar.add(button2);
/*  138 */     toolbar.addSeparator();
/*  139 */     JButton button3 = new JButton(new ImageIcon("images/Save (Custom).png"));
/*  140 */     toolbar.add(button3);
/*  141 */     toolbar.addSeparator();
/*  142 */     JButton button4 = new JButton(new ImageIcon("images/Undo (Custom).png"));
/*  143 */     toolbar.add(button4);
/*  144 */     toolbar.addSeparator();
/*  145 */     JButton button5 = new JButton(new ImageIcon("images/Redo (Custom).png"));
/*  146 */     toolbar.add(button5);
/*  147 */     toolbar.addSeparator();
/*  148 */     JButton button6 = new JButton(new ImageIcon("images/Cut (Custom).png"));
/*  149 */     toolbar.add(button6);
/*  150 */     toolbar.addSeparator();
/*  151 */     JButton button7 = new JButton(new ImageIcon("images/copy (Custom).png"));
/*  152 */     toolbar.add(button7);
/*  153 */     toolbar.addSeparator();
/*  154 */     JButton button8 = new JButton(new ImageIcon("images/Paste (Custom).png"));
/*  155 */     toolbar.add(button8);
/*  156 */     toolbar.addSeparator();
/*  157 */     JButton button9 = new JButton(new ImageIcon("images/bold (Custom).png"));
/*  158 */     toolbar.add(button9);
/*  159 */     toolbar.addSeparator();
/*  160 */     JButton button10 = new JButton(new ImageIcon("images/italic (Custom).png"));
/*  161 */     toolbar.add(button10);
/*  162 */     toolbar.addSeparator();
/*  163 */     JButton button11 = new JButton(new ImageIcon("images/underline (Custom).png"));
/*  164 */     toolbar.add(button11);
/*  165 */     toolbar.addSeparator();
/*  166 */     JButton button12 = new JButton(new SubscriptAction());
/*  167 */     toolbar.add(button12);
/*  168 */     toolbar.addSeparator();
/*  169 */     JButton button13 = new JButton(new SuperscriptAction());
/*  170 */     toolbar.add(button13);
/*  171 */     toolbar.addSeparator();
/*  172 */     JButton button14 = new JButton(new StrikeThroughAction());
/*  173 */     toolbar.add(button14);
/*  174 */     toolbar.addSeparator();
/*  175 */     JButton button15 = new JButton(new StyledEditorKit.AlignmentAction("", 1));
/*  176 */     toolbar.add(button15);
/*  177 */     toolbar.addSeparator();
/*  178 */     JButton button16 = new JButton(new StyledEditorKit.AlignmentAction("", 0));
/*  179 */     toolbar.add(button16);
/*  180 */     toolbar.addSeparator();
/*  181 */     JButton button17 = new JButton(new StyledEditorKit.AlignmentAction("", 2));
/*  182 */     toolbar.add(button17);
/*  183 */     toolbar.addSeparator();
/*  184 */     JButton button18 = new JButton(new ImageIcon("images/unordered (Custom).png"));
/*  185 */     toolbar.add(button18);
/*  186 */     toolbar.addSeparator();
/*  187 */     JButton button24 = new JButton(new ImageIcon("images/bullets (Custom).png"));
/*  188 */     toolbar.add(button24);
/*  189 */     toolbar.addSeparator();
/*  190 */     JButton button22 = new JButton(new ImageIcon("images/images (Custom).jpg"));
/*  191 */     toolbar.add(button22);
/*  192 */     toolbar.addSeparator();
/*  193 */     final JButton button23 = new JButton(new ImageIcon("images/link (Custom).png"));
/*  194 */     toolbar.add(button23);
/*  195 */     toolbar.addSeparator();
/*  196 */     button23.setToolTipText("<html><b>hyperlink</b></html>");
/*  197 */     button23.addActionListener(new ActionListener()
/*      */     {
/*      */
/*      */       public void actionPerformed(ActionEvent e)
/*      */       {
/*  202 */         java.awt.Window parentWindow = javax.swing.SwingUtilities.windowForComponent(button23);
/*  203 */         final JDialog d = new JDialog(parentWindow);
/*  204 */         d.setLocationRelativeTo(null);
/*  205 */         d.setModal(true);
/*  206 */         d.setSize(300, 500);
/*  207 */         d.setLayout(new FlowLayout());
/*  208 */         d.add(new javax.swing.JLabel("Link"));
/*  209 */         final JTextField field = new JTextField(20);
/*  210 */         d.add(field);
/*  211 */         JButton save = new JButton("Save");
/*  212 */         d.add(save);
/*  213 */         save.addActionListener(new ActionListener()
/*      */         {
/*      */           public void actionPerformed(ActionEvent e) {
/*  216 */             String text2 = HTMLEditor.this.textPane.getSelectedText();
/*  217 */             String text = field.getText();
/*  218 */             String text3 = "<p><a href='" + text + "'>" + text2 + "</a></p>";
/*  219 */             text3 = text3 + HTMLEditor.this.textPane.getText();
/*  220 */             HTMLEditor.this.textPane.setText(text3);
/*      */
/*      */           }
/*      */
/*      */
/*  225 */         });
/*  226 */         JButton closeIt = new JButton("Close");
/*  227 */         closeIt.addActionListener(new ActionListener() {
/*      */           public void actionPerformed(ActionEvent e) {
/*  229 */             d.dispose();
/*      */           }
/*  231 */         });
/*  232 */         d.add(closeIt);
/*  233 */         d.setDefaultCloseOperation(2);
/*  234 */         d.pack();
/*  235 */         d.setVisible(true);
/*  236 */         d.setResizable(false);
/*      */       }
/*      */
/*      */
/*  240 */     });
/*  241 */     JButton button19 = new JButton(new ImageIcon("images/palette (Custom).png"));
/*  242 */     button19.setToolTipText("<html><b>color scheme</b></html>");
/*  243 */     button19.addActionListener(new ActionListener()
/*      */     {
/*      */       public void actionPerformed(ActionEvent e)
/*      */       {
/*  247 */         Color initialFG = StyleConstants.getForeground(HTMLEditor.this.textPane.getCharacterAttributes());
/*      */
/*  249 */         Color fgColor = javax.swing.JColorChooser.showDialog(
/*  250 */           HTMLEditor.this.getContentPane(),
/*  251 */           "Foreground",
/*  252 */           initialFG);
/*      */
/*  254 */         if (fgColor == null)
/*      */         {
/*  256 */           return;
/*      */         }
/*      */
/*  259 */         MutableAttributeSet mas = new SimpleAttributeSet();
/*  260 */         StyleConstants.setForeground(mas, fgColor);
/*  261 */         HTMLEditor.this.textPane.setCharacterAttributes(mas, false);
/*      */       }
/*  263 */     });
/*  264 */     toolbar.add(button19);
/*  265 */     toolbar.addSeparator();
/*  266 */     toolbar2.addSeparator();
/*  267 */     JButton button21 = new JButton("<html><font size=3><b>HTML</b></font></html>");
/*  268 */     button21.setPreferredSize(new Dimension(200, 30));
/*  269 */     toolbar2.add(button21);
/*  270 */     toolbar2.addSeparator();
/*  271 */     JButton button20 = new JButton("<html><font size=3><b>Visual</b></font></html>");
/*  272 */     button20.setPreferredSize(new Dimension(200, 30));
/*  273 */     toolbar2.add(button20);
/*  274 */     toolbar2.addSeparator();
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  282 */     button.addActionListener(this);
/*  283 */     button.setActionCommand("New");
/*  284 */     button.setToolTipText("<html><b>new file</b></html>");
/*  285 */     button2.addActionListener(this);
/*  286 */     button2.setActionCommand("Open");
/*  287 */     button2.setToolTipText("<html><b>open</b></html>");
/*  288 */     button3.addActionListener(this);
/*  289 */     button3.setActionCommand("Save");
/*  290 */     button3.setToolTipText("<html><b>save</b></html>");
/*  291 */     button4.addActionListener(this.undoAction);
/*  292 */     button4.setToolTipText("<html><b>undo</b></html>");
/*  293 */     button5.addActionListener(this.redoAction);
/*  294 */     button5.setToolTipText("<html><b>redo</b></html>");
/*  295 */     button6.addActionListener(this.cutAction);
/*  296 */     button6.setToolTipText("<html><b>cut</b></html>");
/*  297 */     button7.addActionListener(this.copyAction);
/*  298 */     button7.setToolTipText("<html><b>copy</b></html>");
/*  299 */     button8.addActionListener(this.pasteAction);
/*  300 */     button8.setToolTipText("<html><b>paste</b></html>");
/*  301 */     button9.addActionListener(this.boldAction);
/*  302 */     button9.setToolTipText("<html><b>bold</b></html>");
/*  303 */     button10.addActionListener(this.italicAction);
/*  304 */     button10.setToolTipText("<html><b>italic</b></html>");
/*  305 */     button11.addActionListener(this.underlineAction);
/*  306 */     button11.setToolTipText("<html><b>underline</b></html>");
/*  307 */     button12.addActionListener(this);
/*  308 */     button12.setIcon(new ImageIcon("images/sub (Custom).gif"));
/*  309 */     button12.setText("");
/*  310 */     button12.setToolTipText("<html><b>sub</b></html>");
/*  311 */     button13.addActionListener(this);
/*  312 */     button13.setIcon(new ImageIcon("images/sup (Custom).gif"));
/*  313 */     button13.setText("");
/*  314 */     button13.setToolTipText("<html><b>sup</b></html>");
/*  315 */     button14.addActionListener(this);
/*  316 */     button14.setIcon(new ImageIcon("images/strick (Custom).png"));
/*  317 */     button14.setText("");
/*  318 */     button14.setToolTipText("<html><b>strike</b></html>");
/*  319 */     button15.addActionListener(this);
/*  320 */     button15.setIcon(new ImageIcon("images/center (Custom).png"));
/*  321 */     button15.setToolTipText("<html><b>center align</b></html>");
/*  322 */     button16.addActionListener(this);
/*  323 */     button16.setIcon(new ImageIcon("images/left (Custom).png"));
/*  324 */     button16.setToolTipText("<html><b>left align</b></html>");
/*  325 */     button17.addActionListener(this);
/*  326 */     button17.setIcon(new ImageIcon("images/right (Custom).png"));
/*  327 */     button17.setToolTipText("<html><b>right align</b></html>");
/*  328 */     button18.addActionListener(this.unorderedListAction);
/*  329 */     button18.setToolTipText("<html><b>unordered list</b></html>");
/*  330 */     button24.addActionListener(this.bulletAction);
/*  331 */     button24.setToolTipText("<html><b>bullets</b></html>");
/*  332 */     button20.addActionListener(this);
/*  333 */     button20.setActionCommand("visual");
/*  334 */     button20.setToolTipText("<html><b>see visual</b></html>");
/*  335 */     button21.addActionListener(this);
/*  336 */     button21.setActionCommand("HTMLSource");
/*  337 */     button21.setToolTipText("<html><b>see html codes</b></html>");
/*  338 */     button22.addActionListener(this);
/*  339 */     button22.setToolTipText("<html><b>image</b></html>");
/*  340 */     button22.setActionCommand("image");
/*      */
/*  342 */     JMenuBar menuBar = new JMenuBar();
/*      */
/*  344 */     JMenu fileMenu = new JMenu("<html><font size=4>File</font></html>");
/*  345 */     JMenu editMenu = new JMenu("<html><font size=4>Edit</font></html>");
/*  346 */     JMenu colorMenu = new JMenu("<html><font size=4>Color</font></html>");
/*  347 */     JMenu fontMenu = new JMenu("<html><font size=4>Font</font></html>");
/*  348 */     JMenu styleMenu = new JMenu("<html><font size=4>Style</font></html>");
/*  349 */     JMenu alignMenu = new JMenu("<html><font size=4>Align</font></html>");
/*  350 */     JMenu inserMenu = new JMenu("<html><font size=4>Insert</font></html>");
/*  351 */     JMenu helpMenu = new JMenu("<html><font size=4>Help</font></html>");
/*  352 */     JMenu viewMenu = new JMenu("<html><font size=4>View</font></html>");
/*      */
/*  354 */     menuBar.add(fileMenu);
/*  355 */     menuBar.add(editMenu);
/*  356 */     menuBar.add(colorMenu);
/*  357 */     menuBar.add(fontMenu);
/*  358 */     menuBar.add(styleMenu);
/*  359 */     menuBar.add(alignMenu);
/*  360 */     menuBar.add(inserMenu);
/*  361 */     menuBar.add(helpMenu);
/*  362 */     menuBar.add(viewMenu);
/*      */
/*  364 */     JMenuItem newItem = new JMenuItem("New", new ImageIcon("newfile.png"));
/*  365 */     JMenuItem openItem = new JMenuItem("Open", new ImageIcon("newfolder.gif"));
/*  366 */     JMenuItem saveItem = new JMenuItem("Save", new ImageIcon("save.png"));
/*  367 */     JMenuItem saveAsItem = new JMenuItem("Save As");
/*  368 */     JMenuItem exitItem = new JMenuItem("Exit");
/*      */
/*  370 */     newItem.addActionListener(this);
/*  371 */     openItem.addActionListener(this);
/*  372 */     saveItem.addActionListener(this);
/*  373 */     saveAsItem.addActionListener(this);
/*  374 */     exitItem.addActionListener(this);
/*      */
/*  376 */     fileMenu.add(newItem);
/*  377 */     fileMenu.add(openItem);
/*  378 */     fileMenu.add(saveItem);
/*  379 */     fileMenu.add(saveAsItem);
/*  380 */     fileMenu.add(exitItem);
/*      */
/*      */
/*  383 */     JMenuItem undoItem = new JMenuItem(this.undoAction);
/*  384 */     undoItem.setIcon(new ImageIcon("undo.png"));
/*  385 */     JMenuItem redoItem = new JMenuItem(this.redoAction);
/*  386 */     redoItem.setIcon(new ImageIcon("redo.png"));
/*  387 */     JMenuItem cutItem = new JMenuItem(this.cutAction);
/*  388 */     cutItem.setIcon(new ImageIcon("cut.png"));
/*  389 */     JMenuItem copyItem = new JMenuItem(this.copyAction);
/*  390 */     copyItem.setIcon(new ImageIcon("copy.png"));
/*  391 */     JMenuItem pasteItem = new JMenuItem(this.pasteAction);
/*  392 */     pasteItem.setIcon(new ImageIcon("paste.png"));
/*  393 */     JMenuItem clearItem = new JMenuItem("Clear");
/*  394 */     JMenuItem selectAllItem = new JMenuItem("Select All");
/*  395 */     JMenuItem insertBreaKItem = new JMenuItem(this.insertBreakAction);
/*  396 */     JMenuItem unorderedListItem = new JMenuItem(this.unorderedListAction);
/*      */
/*  398 */     unorderedListItem.setIcon(new ImageIcon("unorderedlist.png"));
/*  399 */     JMenuItem bulletItem = new JMenuItem(this.bulletAction);
/*      */
/*  401 */     cutItem.setText("Cut");
/*  402 */     copyItem.setText("Copy");
/*  403 */     pasteItem.setText("Paste");
/*  404 */     insertBreaKItem.setText("Break");
/*      */
/*  406 */     clearItem.addActionListener(this);
/*  407 */     selectAllItem.addActionListener(this);
/*      */
/*  409 */     editMenu.add(undoItem);
/*  410 */     editMenu.add(redoItem);
/*  411 */     editMenu.add(cutItem);
/*  412 */     editMenu.add(copyItem);
/*  413 */     editMenu.add(pasteItem);
/*  414 */     editMenu.add(clearItem);
/*  415 */     editMenu.add(selectAllItem);
/*  416 */     editMenu.add(insertBreaKItem);
/*  417 */     editMenu.add(unorderedListItem);
/*      */
/*  419 */     editMenu.add(bulletItem);
/*      */
/*  421 */     JMenuItem redTextItem = new JMenuItem(new StyledEditorKit.ForegroundAction("Red", Color.red));
/*  422 */     JMenuItem orangeTextItem = new JMenuItem(new StyledEditorKit.ForegroundAction("Orange", Color.orange));
/*  423 */     JMenuItem yellowTextItem = new JMenuItem(new StyledEditorKit.ForegroundAction("Yellow", Color.yellow));
/*  424 */     JMenuItem greenTextItem = new JMenuItem(new StyledEditorKit.ForegroundAction("Green", Color.green));
/*  425 */     JMenuItem blueTextItem = new JMenuItem(new StyledEditorKit.ForegroundAction("Blue", Color.blue));
/*  426 */     JMenuItem cyanTextItem = new JMenuItem(new StyledEditorKit.ForegroundAction("Cyan", Color.cyan));
/*  427 */     JMenuItem magentaTextItem = new JMenuItem(new StyledEditorKit.ForegroundAction("Magenta", Color.magenta));
/*  428 */     JMenuItem blackTextItem = new JMenuItem(new StyledEditorKit.ForegroundAction("Black", Color.black));
/*  429 */     JMenuItem pinkTextItem = new JMenuItem(new StyledEditorKit.ForegroundAction("Pink", Color.pink));
/*      */
/*      */
/*      */
/*  433 */     colorMenu.add(redTextItem);
/*  434 */     colorMenu.add(orangeTextItem);
/*  435 */     colorMenu.add(yellowTextItem);
/*  436 */     colorMenu.add(greenTextItem);
/*  437 */     colorMenu.add(blueTextItem);
/*  438 */     colorMenu.add(cyanTextItem);
/*  439 */     colorMenu.add(magentaTextItem);
/*  440 */     colorMenu.add(blackTextItem);
/*  441 */     colorMenu.add(pinkTextItem);
/*      */
/*  443 */     JMenu fontTypeMenu = new JMenu("Font Type");
/*  444 */     fontMenu.add(fontTypeMenu);
/*      */
/*  446 */     String[] fontTypes = { "SansSerif", "Serif", "Monospaced", "Dialog", "DialogInput" };
/*  447 */     for (int i = 0; i < fontTypes.length; i++) {
/*  448 */       if (this.debug) System.out.println(fontTypes[i]);
/*  449 */       JMenuItem nextTypeItem = new JMenuItem(fontTypes[i]);
/*  450 */       nextTypeItem.setAction(new javax.swing.text.StyledEditorKit.FontFamilyAction(fontTypes[i], fontTypes[i]));
/*  451 */       fontTypeMenu.add(nextTypeItem);
/*      */     }
/*      */
/*  454 */     JMenu fontSizeMenu = new JMenu("Font Size");
/*  455 */     fontMenu.add(fontSizeMenu);
/*      */
/*  457 */     int[] fontSizes = { 6, 8, 10, 12, 14, 16, 20, 24, 32, 36, 48, 72 };
/*  458 */     for (int i = 0; i < fontSizes.length; i++) {
/*  459 */       if (this.debug) System.out.println(fontSizes[i]);
/*  460 */       JMenuItem nextSizeItem = new JMenuItem(String.valueOf(fontSizes[i]));
/*  461 */       nextSizeItem.setAction(new javax.swing.text.StyledEditorKit.FontSizeAction(String.valueOf(fontSizes[i]), fontSizes[i]));
/*  462 */       fontSizeMenu.add(nextSizeItem);
/*      */     }
/*      */
/*      */
/*  466 */     JMenuItem boldMenuItem = new JMenuItem(this.boldAction);
/*  467 */     boldMenuItem.setIcon(new ImageIcon("bold.png"));
/*  468 */     JMenuItem underlineMenuItem = new JMenuItem(this.underlineAction);
/*  469 */     underlineMenuItem.setIcon(new ImageIcon("underline.png"));
/*  470 */     JMenuItem italicMenuItem = new JMenuItem(this.italicAction);
/*  471 */     italicMenuItem.setIcon(new ImageIcon("italic.png"));
/*  472 */     boldMenuItem.setText("Bold");
/*  473 */     underlineMenuItem.setText("Underline");
/*  474 */     italicMenuItem.setText("Italic");
/*      */
/*      */
/*  477 */     styleMenu.add(boldMenuItem);
/*  478 */     styleMenu.add(underlineMenuItem);
/*  479 */     styleMenu.add(italicMenuItem);
/*      */
/*  481 */     JMenuItem subscriptMenuItem = new JMenuItem(new SubscriptAction());
/*  482 */     subscriptMenuItem.setIcon(new ImageIcon("sub.png"));
/*  483 */     JMenuItem superscriptMenuItem = new JMenuItem(new SuperscriptAction());
/*  484 */     superscriptMenuItem.setIcon(new ImageIcon("sup.png"));
/*  485 */     JMenuItem strikeThroughMenuItem = new JMenuItem(new StrikeThroughAction());
/*  486 */     strikeThroughMenuItem.setIcon(new ImageIcon("strike.png"));
/*      */
/*  488 */     subscriptMenuItem.setText("Subscript");
/*  489 */     superscriptMenuItem.setText("Superscript");
/*  490 */     strikeThroughMenuItem.setText("StrikeThrough");
/*      */
/*      */
/*  493 */     styleMenu.add(subscriptMenuItem);
/*  494 */     styleMenu.add(superscriptMenuItem);
/*  495 */     styleMenu.add(strikeThroughMenuItem);
/*      */
/*      */
/*  498 */     JMenuItem leftAlignMenuItem = new JMenuItem(new StyledEditorKit.AlignmentAction("Left Align", 0));
/*  499 */     leftAlignMenuItem.setIcon(new ImageIcon("left.png"));
/*  500 */     JMenuItem centerMenuItem = new JMenuItem(new StyledEditorKit.AlignmentAction("Center", 1));
/*  501 */     centerMenuItem.setIcon(new ImageIcon("center.png"));
/*  502 */     JMenuItem rightAlignMenuItem = new JMenuItem(new StyledEditorKit.AlignmentAction("Right Align", 2));
/*  503 */     rightAlignMenuItem.setIcon(new ImageIcon("right.png"));
/*  504 */     leftAlignMenuItem.setText("Left Align");
/*  505 */     centerMenuItem.setText("Center");
/*  506 */     rightAlignMenuItem.setText("Right Align");
/*      */
/*      */
/*      */
/*  510 */     alignMenu.add(leftAlignMenuItem);
/*  511 */     alignMenu.add(centerMenuItem);
/*  512 */     alignMenu.add(rightAlignMenuItem);
/*      */
/*  514 */     JMenuItem helpItem = new JMenuItem("Help");
/*  515 */     helpItem.addActionListener(this);
/*  516 */     helpMenu.add(helpItem);
/*      */
/*  518 */     JMenuItem shortcutsItem = new JMenuItem("Keyboard Shortcuts");
/*  519 */     shortcutsItem.addActionListener(this);
/*  520 */     helpMenu.add(shortcutsItem);
/*      */
/*  522 */     JMenuItem shortcutsItem2 = new JMenuItem("Color Scheme");
/*  523 */     shortcutsItem2.setIcon(new ImageIcon("info.png"));
/*  524 */     shortcutsItem2.addActionListener(new ActionListener()
/*      */     {
/*      */       public void actionPerformed(ActionEvent e)
/*      */       {
/*  528 */         Color initialFG = StyleConstants.getForeground(HTMLEditor.this.textPane.getCharacterAttributes());
/*      */
/*  530 */         Color fgColor = javax.swing.JColorChooser.showDialog(
/*  531 */           HTMLEditor.this.getContentPane(),
/*  532 */           "Foreground",
/*  533 */           initialFG);
/*      */
/*  535 */         if (fgColor == null)
/*      */         {
/*  537 */           return;
/*      */         }
/*      */
/*  540 */         MutableAttributeSet mas = new SimpleAttributeSet();
/*  541 */         StyleConstants.setForeground(mas, fgColor);
/*  542 */         HTMLEditor.this.textPane.setCharacterAttributes(mas, false);
/*      */       }
/*  544 */     });
/*  545 */     helpMenu.add(shortcutsItem2);
/*      */
/*      */
/*  548 */     JMenuItem HTMLItem = new javax.swing.JRadioButtonMenuItem("visual");
/*  549 */     JMenuItem PlainItem = new javax.swing.JRadioButtonMenuItem("HTMLSource");
/*  550 */     ButtonGroup myGroup = new ButtonGroup();
/*  551 */     HTMLItem.setSelected(true);
/*  552 */     HTMLItem.addActionListener(this);
/*  553 */     PlainItem.setSelected(true);
/*  554 */     PlainItem.addActionListener(this);
/*  555 */     viewMenu.add(HTMLItem);
/*  556 */     viewMenu.add(PlainItem);
/*  557 */     myGroup.add(HTMLItem);
/*  558 */     myGroup.add(PlainItem);
/*      */
/*      */
/*      */
/*  562 */     JMenuItem Image = new JMenuItem("image", new ImageIcon("insertimage.png"));
/*  563 */     inserMenu.add(Image);
/*  564 */     Image.addActionListener(new ActionListener()
/*      */     {
/*      */       public void actionPerformed(ActionEvent e) {
/*  567 */         HTMLEditor.this.insertActionPerformed();
/*      */
/*      */       }
/*      */
/*      */
/*  572 */     });
/*  573 */     JMenuItem hyperlink = new JMenuItem("hyperlink");
/*  574 */     inserMenu.add(hyperlink);
/*  575 */     hyperlink.addActionListener(new ActionListener()
/*      */     {
/*      */
/*      */       public void actionPerformed(ActionEvent e)
/*      */       {
/*  580 */         java.awt.Window parentWindow = javax.swing.SwingUtilities.windowForComponent(button23);
/*  581 */         final JDialog d = new JDialog(parentWindow);
/*  582 */         d.setLocationRelativeTo(null);
/*  583 */         d.setModal(true);
/*  584 */         d.setSize(300, 500);
/*  585 */         d.setLayout(new FlowLayout());
/*  586 */         d.add(new javax.swing.JLabel("Link"));
/*  587 */         final JTextField field = new JTextField(20);
/*  588 */         d.add(field);
/*  589 */         JButton save = new JButton("Save");
/*  590 */         d.add(save);
/*  591 */         save.addActionListener(new ActionListener()
/*      */         {
/*      */           public void actionPerformed(ActionEvent e) {
/*  594 */             String text2 = HTMLEditor.this.textPane.getSelectedText();
/*  595 */             String text = field.getText();
/*  596 */             String text3 = "<p><a href='" + text + "'>" + text2 + "</a></p>";
/*  597 */             text3 = text3 + HTMLEditor.this.textPane.getText();
/*  598 */             HTMLEditor.this.textPane.setText(text3);
/*      */
/*      */           }
/*      */
/*      */
/*  603 */         });
/*  604 */         JButton closeIt = new JButton("Close");
/*  605 */         closeIt.addActionListener(new ActionListener() {
/*      */           public void actionPerformed(ActionEvent e) {
/*  607 */             d.dispose();
/*      */           }
/*  609 */         });
/*  610 */         d.add(closeIt);
/*  611 */         d.setDefaultCloseOperation(2);
/*  612 */         d.pack();
/*  613 */         d.setVisible(true);
/*  614 */         d.setResizable(false);
/*      */       }
/*      */
/*      */
/*  618 */     });
/*  619 */     JPanel editorControlPanel = new JPanel();
/*  620 */     editorControlPanel.setLayout(new java.awt.GridLayout(3, 3));
/*      */
/*      */
/*      */
/*      */
/*      */
/*  626 */     JButton cutButton = new JButton(this.cutAction);
/*  627 */     JButton copyButton = new JButton(this.copyAction);
/*  628 */     JButton pasteButton = new JButton(this.pasteAction);
/*      */
/*  630 */     JButton boldButton = new JButton(this.boldAction);
/*  631 */     JButton underlineButton = new JButton(this.underlineAction);
/*  632 */     JButton italicButton = new JButton(this.italicAction);
/*      */
/*      */
/*      */
/*  636 */     JButton insertBreakButton = new JButton(this.insertBreakAction);
/*      */
/*      */
/*  639 */     cutButton.setText("Cut");
/*  640 */     copyButton.setText("Copy");
/*  641 */     pasteButton.setText("Paste");
/*      */
/*  643 */     boldButton.setText("Bold");
/*  644 */     underlineButton.setText("Underline");
/*  645 */     italicButton.setText("Italic");
/*      */
/*      */
/*  648 */     insertBreakButton.setText("Insert Break");
/*      */
/*      */
/*      */
/*      */
/*  653 */     editorControlPanel.add(cutButton);
/*  654 */     editorControlPanel.add(copyButton);
/*  655 */     editorControlPanel.add(pasteButton);
/*      */
/*  657 */     editorControlPanel.add(boldButton);
/*  658 */     editorControlPanel.add(underlineButton);
/*  659 */     editorControlPanel.add(italicButton);
/*      */
/*      */
/*      */
/*  663 */     editorControlPanel.add(insertBreakButton);
/*      */
/*      */
/*  666 */     JButton subscriptButton = new JButton(new SubscriptAction());
/*  667 */     JButton superscriptButton = new JButton(new SuperscriptAction());
/*  668 */     JButton strikeThroughButton = new JButton(new StrikeThroughAction());
/*      */
/*      */
/*      */
/*  672 */     JPanel specialPanel = new JPanel();
/*  673 */     specialPanel.setLayout(new FlowLayout());
/*      */
/*  675 */     specialPanel.add(subscriptButton);
/*  676 */     specialPanel.add(superscriptButton);
/*  677 */     specialPanel.add(strikeThroughButton);
/*      */
/*      */
/*      */
/*      */
/*      */
/*  683 */     JButton leftAlignButton = new JButton(new StyledEditorKit.AlignmentAction("Left Align", 0));
/*  684 */     JButton centerButton = new JButton(new StyledEditorKit.AlignmentAction("Center", 1));
/*  685 */     JButton rightAlignButton = new JButton(new StyledEditorKit.AlignmentAction("Right Align", 2));
/*      */
/*      */
/*      */
/*  689 */     leftAlignButton.setText("Left Align");
/*  690 */     centerButton.setText("Center");
/*  691 */     rightAlignButton.setText("Right Align");
/*      */
/*  693 */     JPanel alignPanel = new JPanel();
/*  694 */     alignPanel.setLayout(new FlowLayout());
/*  695 */     alignPanel.add(leftAlignButton);
/*  696 */     alignPanel.add(centerButton);
/*  697 */     alignPanel.add(rightAlignButton);
/*      */
/*  699 */     this.document.addUndoableEditListener(this.undoHandler);
/*  700 */     resetUndoManager();
/*      */
/*      */
/*      */
/*      */
/*      */
/*  706 */     this.textPane = new JTextPane(this.document);
/*  707 */     this.textPane.setDocument(this.document);
/*      */
/*  709 */     this.textPane.setContentType("text/html");
/*  710 */     this.textPane2.setContentType("text/plain");
/*  711 */     String text = null;
/*  712 */     text = this.textPane.getText();
/*  713 */     this.textPane2.setContentType("text/plain");
/*  714 */     this.textPane2.setText(text);
/*  715 */     this.textPane2.setBackground(Color.DARK_GRAY);
/*  716 */     this.textPane2.setForeground(Color.WHITE);
/*  717 */     this.textPane.getInputMap().put(javax.swing.KeyStroke.getKeyStroke(' '), "nbsp");
/*  718 */     this.textPane.getActionMap().put("nbsp", new AbstractAction()
/*      */     {
/*      */
/*      */       public void actionPerformed(ActionEvent e)
/*      */       {
/*  723 */         HTMLEditor.this.textPane.replaceSelection(" ");
/*      */       }
/*  725 */     });
/*  726 */     JScrollPane scrollPane = new JScrollPane(this.textPane);
/*  727 */     JScrollPane scrollPane2 = new JScrollPane(this.textPane2);
/*      */
/*  729 */     Dimension scrollPaneSize = new Dimension(455, 500);
/*  730 */     Dimension scrollPaneSize2 = new Dimension(460, 500);
/*  731 */     scrollPane.setPreferredSize(scrollPaneSize);
/*  732 */     scrollPane2.setPreferredSize(scrollPaneSize2);
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  741 */     setJMenuBar(menuBar);
/*  742 */     getContentPane().add(scrollPane, "West");
/*  743 */     getContentPane().add(scrollPane2, "East");
/*  744 */     getContentPane().add(toolbar, "North");
/*  745 */     getContentPane().add(buttonPane, "South");
/*  746 */     pack();
/*  747 */     setLocationRelativeTo(null);
/*  748 */     startNewDocument();
/*  749 */     show();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public void actionPerformed(ActionEvent ae)
/*      */   {
/*  758 */     String actionCommand = ae.getActionCommand();
/*  759 */     if (this.debug) {
/*  760 */       int modifier = ae.getModifiers();
/*  761 */       long when = ae.getWhen();
/*  762 */       String parameter = ae.paramString();
/*  763 */       System.out.println("actionCommand: " + actionCommand);
/*  764 */       System.out.println("modifier: " + modifier);
/*  765 */       System.out.println("when: " + when);
/*  766 */       System.out.println("parameter: " + parameter);
/*      */     }
/*  768 */     if (actionCommand.compareTo("New") == 0) {
/*  769 */       startNewDocument();
/*  770 */     } else if (actionCommand.compareTo("Open") == 0) {
/*  771 */       openDocument();
/*  772 */     } else if (actionCommand.compareTo("Save") == 0) {
/*  773 */       saveDocument();
/*  774 */     } else if (actionCommand.compareTo("Save As") == 0) {
/*  775 */       saveDocumentAs();
/*  776 */     } else if (actionCommand.compareTo("Exit") == 0) {
/*  777 */       exit();
/*  778 */     } else if (actionCommand.compareTo("Clear") == 0) {
/*  779 */       clear();
/*  780 */     } else if (actionCommand.compareTo("Select All") == 0) {
/*  781 */       selectAll();
/*  782 */     } else if (actionCommand.compareTo("Help") == 0) {
/*  783 */       help();
/*  784 */     } else if (actionCommand.compareTo("Keyboard Shortcuts") == 0) {
/*  785 */       showShortcuts();
/*  786 */     } else if (actionCommand.compareTo("Color Scheme") == 0) {
/*  787 */       showShortcuts2();
/*  788 */     } else if (actionCommand.compareTo("visual") == 0) {
/*  789 */       HTML();
/*  790 */     } else if (actionCommand.compareTo("HTMLSource") == 0) {
/*  791 */       Plain();
/*  792 */     } else if (actionCommand.compareTo("image") == 0) {
/*  793 */       insertActionPerformed();
/*  794 */     } else if (actionCommand.compareTo("table") == 0) {
/*  795 */       AddingTables();
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public void AddingTables() {}
/*      */
/*      */   public void HTML()
/*      */   {
/*  804 */     String text = null;
/*  805 */     text = this.textPane2.getText();
/*  806 */     this.textPane.setContentType("text/html");
/*  807 */     this.textPane.setText(text);
/*  808 */     this.textPane.setBackground(Color.WHITE);
/*      */   }
/*      */
/*      */
/*      */   public void Plain()
/*      */   {
/*  814 */     String text = null;
/*  815 */     text = this.textPane.getText();
/*  816 */     this.textPane2.setContentType("text/plain");
/*  817 */     this.textPane2.setText(text);
/*  818 */     this.textPane2.setBackground(Color.DARK_GRAY);
/*  819 */     this.textPane2.setForeground(Color.WHITE);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void resetUndoManager()
/*      */   {
/*  832 */     this.undo.discardAllEdits();
/*  833 */     this.undoAction.update();
/*  834 */     this.redoAction.update();
/*      */   }
/*      */
/*      */
/*      */   public void startNewDocument()
/*      */   {
/*  840 */     Document oldDoc = this.textPane.getDocument();
/*  841 */     if (oldDoc != null)
/*  842 */       oldDoc.removeUndoableEditListener(this.undoHandler);
/*  843 */     HTMLEditorKit editorKit = new HTMLEditorKit();
/*  844 */     this.document = ((HTMLDocument)editorKit.createDefaultDocument());
/*  845 */     this.textPane.setDocument(this.document);
/*  846 */     this.currentFile = null;
/*  847 */     setTitle("HTMLEditor");
/*  848 */     this.textPane.getDocument().addUndoableEditListener(this.undoHandler);
/*  849 */     resetUndoManager();
/*      */   }
/*      */
/*      */   public void openDocument() {
/*      */     try {
/*  854 */       File current = new File(".");
/*  855 */       JFileChooser chooser = new JFileChooser(current);
/*  856 */       chooser.setFileSelectionMode(2);
/*  857 */       chooser.setFileFilter(new HTMLFileFilter());
/*  858 */       int approval = chooser.showSaveDialog(this);
/*  859 */       if (approval == 0) {
/*  860 */         this.currentFile = chooser.getSelectedFile();
/*  861 */         setTitle(this.currentFile.getName());
/*  862 */         java.io.FileReader fr = new java.io.FileReader(this.currentFile);
/*  863 */         Document oldDoc = this.textPane.getDocument();
/*  864 */         if (oldDoc != null)
/*  865 */           oldDoc.removeUndoableEditListener(this.undoHandler);
/*  866 */         HTMLEditorKit editorKit = new HTMLEditorKit();
/*  867 */         this.document = ((HTMLDocument)editorKit.createDefaultDocument());
/*  868 */         editorKit.read(fr, this.document, 0);
/*  869 */         this.document.addUndoableEditListener(this.undoHandler);
/*  870 */         this.textPane.setDocument(this.document);
/*  871 */         resetUndoManager();
/*      */       }
/*      */     } catch (javax.swing.text.BadLocationException ble) {
/*  874 */       System.err.println("BadLocationException: " + ble.getMessage());
/*      */     } catch (FileNotFoundException fnfe) {
/*  876 */       System.err.println("FileNotFoundException: " + fnfe.getMessage());
/*      */     } catch (IOException ioe) {
/*  878 */       System.err.println("IOException: " + ioe.getMessage());
/*      */     }
/*      */   }
/*      */
/*      */   public void saveDocument()
/*      */   {
/*  884 */     if (this.currentFile != null) {
/*      */       try {
/*  886 */         FileWriter fw = new FileWriter(this.currentFile);
/*  887 */         fw.write(this.textPane.getText());
/*  888 */         fw.close();
/*      */       } catch (FileNotFoundException fnfe) {
/*  890 */         System.err.println("FileNotFoundException: " + fnfe.getMessage());
/*      */       } catch (IOException ioe) {
/*  892 */         System.err.println("IOException: " + ioe.getMessage());
/*      */       }
/*      */     } else {
/*  895 */       saveDocumentAs();
/*      */     }
/*      */   }
/*      */
/*      */   public void saveDocumentAs() {
/*      */     try {
/*  901 */       File current = new File(".");
/*  902 */       JFileChooser chooser = new JFileChooser(current);
/*  903 */       chooser.setFileSelectionMode(2);
/*  904 */       chooser.setFileFilter(new HTMLFileFilter());
/*  905 */       int approval = chooser.showSaveDialog(this);
/*  906 */       if (approval == 0) {
/*  907 */         File newFile = chooser.getSelectedFile();
/*  908 */         if (newFile.exists()) {
/*  909 */           String message = newFile.getAbsolutePath() +
/*  910 */             " already exists. \n" +
/*  911 */             "Do you want to replace it?";
/*  912 */           if (JOptionPane.showConfirmDialog(this, message) == 0) {
/*  913 */             this.currentFile = newFile;
/*  914 */             setTitle(this.currentFile.getName());
/*  915 */             FileWriter fw = new FileWriter(this.currentFile);
/*  916 */             fw.write(this.textPane.getText());
/*  917 */             fw.close();
/*  918 */             if (this.debug) System.out.println("Saved " + this.currentFile.getAbsolutePath());
/*      */           }
/*      */         } else {
/*  921 */           this.currentFile = new File(newFile.getAbsolutePath());
/*  922 */           setTitle(this.currentFile.getName());
/*  923 */           FileWriter fw = new FileWriter(this.currentFile);
/*  924 */           fw.write(this.textPane.getText());
/*  925 */           fw.close();
/*  926 */           if (this.debug) System.out.println("Saved " + this.currentFile.getAbsolutePath());
/*      */         }
/*      */       }
/*      */     } catch (FileNotFoundException fnfe) {
/*  930 */       System.err.println("FileNotFoundException: " + fnfe.getMessage());
/*      */     } catch (IOException ioe) {
/*  932 */       System.err.println("IOException: " + ioe.getMessage());
/*      */     }
/*      */   }
/*      */
/*      */   public void exit() {
/*  937 */     String exitMessage = "Are you sure you want to exit?";
/*  938 */     if (JOptionPane.showConfirmDialog(this, exitMessage) == 0) {
/*  939 */       System.exit(3);
/*      */     }
/*      */   }
/*      */
/*      */   public void clear() {
/*  944 */     startNewDocument();
/*      */   }
/*      */
/*      */   public void selectAll() {
/*  948 */     this.textPane.selectAll();
/*      */   }
/*      */
/*      */   public void help() {
/*  952 */     JOptionPane.showMessageDialog(this, "tansuaksan@gmail.com\nsource reference: CopyPastefromHyperspce Programming Services\n");
/*      */   }
/*      */
/*      */   public void showShortcuts() {
/*  956 */     String shortcuts = "Navigate in    |  Tab\nNavigate out   |  Ctrl+Tab\nNavigate out backwards    |  Shift+Ctrl+Tab\nMove up/down a line    |  Up/Down Arrown\nMove left/right a component or char    |  Left/Right Arrow\nMove up/down one vertical block    |  PgUp/PgDn\nMove to start/end of line    |  Home/End\nMove to previous/next word    |  Ctrl+Left/Right Arrow\nMove to start/end of data    |  Ctrl+Home/End\nMove left/right one block    |  Ctrl+PgUp/PgDn\nSelect All    |  Ctrl+A\nExtend selection up one line    |  Shift+Up Arrow\nExtend selection down one line    |  Shift+Down Arrow\nExtend selection to beginning of line    |  Shift+Home\nExtend selection to end of line    |  Shift+End\nExtend selection to beginning of data    |  Ctrl+Shift+Home\nExtend selection to end of data    |  Ctrl+Shift+End\nExtend selection left    |  Shift+Right Arrow\nExtend selection right    |  Shift+Right Arrow\nExtend selection up one vertical block    |  Shift+PgUp\nExtend selection down one vertical block    |  Shift+PgDn\nExtend selection left one block    |  Ctrl+Shift+PgUp\nExtend selection right one block    |  Ctrl+Shift+PgDn\nExtend selection left one word    |  Ctrl+Shift+Left Arrow\nExtend selection right one word    |  Ctrl+Shift+Right Arrow\n";
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  981 */     JOptionPane.showMessageDialog(this, shortcuts);
/*      */   }
/*      */
/*  984 */   public void showShortcuts2() { ImageIcon icon = new ImageIcon("info2.png");
/*  985 */     JOptionPane.showMessageDialog(null, "", "Color Scheme", 1, icon);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   class SubscriptAction
/*      */     extends StyledEditorKit.StyledTextAction
/*      */   {
/* 1003 */     public SubscriptAction() { super(); }
/*      */
/*      */     public void actionPerformed(ActionEvent ae) {
/* 1006 */       javax.swing.JEditorPane editor = getEditor(ae);
/* 1007 */       if (editor != null) {
/* 1008 */         StyledEditorKit kit = getStyledEditorKit(editor);
/* 1009 */         MutableAttributeSet attr = kit.getInputAttributes();
/* 1010 */         boolean subscript = !StyleConstants.isSubscript(attr);
/* 1011 */         SimpleAttributeSet sas = new SimpleAttributeSet();
/* 1012 */         StyleConstants.setSubscript(sas, subscript);
/* 1013 */         setCharacterAttributes(editor, sas, false);
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   class SuperscriptAction
/*      */     extends StyledEditorKit.StyledTextAction
/*      */   {
/* 1024 */     public SuperscriptAction() { super(); }
/*      */
/*      */     public void actionPerformed(ActionEvent ae) {
/* 1027 */       javax.swing.JEditorPane editor = getEditor(ae);
/* 1028 */       if (editor != null) {
/* 1029 */         StyledEditorKit kit = getStyledEditorKit(editor);
/* 1030 */         MutableAttributeSet attr = kit.getInputAttributes();
/* 1031 */         boolean superscript = !StyleConstants.isSuperscript(attr);
/* 1032 */         SimpleAttributeSet sas = new SimpleAttributeSet();
/* 1033 */         StyleConstants.setSuperscript(sas, superscript);
/* 1034 */         setCharacterAttributes(editor, sas, false);
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   class StrikeThroughAction
/*      */     extends StyledEditorKit.StyledTextAction
/*      */   {
/*      */     public StrikeThroughAction()
/*      */     {
/* 1046 */       super();
/*      */     }
/*      */
/*      */     public void actionPerformed(ActionEvent ae) {
/* 1050 */       javax.swing.JEditorPane editor = getEditor(ae);
/* 1051 */       if (editor != null) {
/* 1052 */         StyledEditorKit kit = getStyledEditorKit(editor);
/* 1053 */         MutableAttributeSet attr = kit.getInputAttributes();
/* 1054 */         boolean strikeThrough = !StyleConstants.isStrikeThrough(attr);
/* 1055 */         SimpleAttributeSet sas = new SimpleAttributeSet();
/* 1056 */         StyleConstants.setStrikeThrough(sas, strikeThrough);
/* 1057 */         setCharacterAttributes(editor, sas, false);
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */   class HTMLFileFilter extends javax.swing.filechooser.FileFilter {
/*      */     HTMLFileFilter() {}
/*      */
/*      */     public boolean accept(File f) {
/* 1066 */       return (f.isDirectory()) || (f.getName().toLowerCase().indexOf("*.html") > 0);
/*      */     }
/*      */
/*      */     public String getDescription() {
/* 1070 */       return "*.html";
/*      */     }
/*      */   }
/*      */
/*      */   class UndoHandler implements javax.swing.event.UndoableEditListener
/*      */   {
/*      */     UndoHandler() {}
/*      */
/*      */     public void undoableEditHappened(javax.swing.event.UndoableEditEvent e)
/*      */     {
/* 1080 */       HTMLEditor.this.undo.addEdit(e.getEdit());
/* 1081 */       HTMLEditor.this.undoAction.update();
/* 1082 */       HTMLEditor.this.redoAction.update();
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   class UndoAction
/*      */     extends AbstractAction
/*      */   {
/*      */     public UndoAction()
/*      */     {
/* 1092 */       super();
/* 1093 */       setEnabled(false);
/*      */     }
/*      */
/*      */     public void actionPerformed(ActionEvent e) {
/*      */       try {
/* 1098 */         HTMLEditor.this.undo.undo();
/*      */       } catch (javax.swing.undo.CannotUndoException ex) {
/* 1100 */         System.out.println("Unable to undo: " + ex);
/* 1101 */         ex.printStackTrace();
/*      */       }
/* 1103 */       update();
/* 1104 */       HTMLEditor.this.redoAction.update();
/*      */     }
/*      */
/*      */     protected void update() {
/* 1108 */       if (HTMLEditor.this.undo.canUndo()) {
/* 1109 */         setEnabled(true);
/* 1110 */         putValue("Name", HTMLEditor.this.undo.getUndoPresentationName());
/*      */       } else {
/* 1112 */         setEnabled(false);
/* 1113 */         putValue("Name", "Undo");
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   class RedoAction
/*      */     extends AbstractAction
/*      */   {
/*      */     public RedoAction()
/*      */     {
/* 1125 */       super();
/* 1126 */       setEnabled(false);
/*      */     }
/*      */
/*      */     public void actionPerformed(ActionEvent e) {
/*      */       try {
/* 1131 */         HTMLEditor.this.undo.redo();
/*      */       } catch (javax.swing.undo.CannotRedoException ex) {
/* 1133 */         System.err.println("Unable to redo: " + ex);
/* 1134 */         ex.printStackTrace();
/*      */       }
/* 1136 */       update();
/* 1137 */       HTMLEditor.this.undoAction.update();
/*      */     }
/*      */
/*      */     protected void update() {
/* 1141 */       if (HTMLEditor.this.undo.canRedo()) {
/* 1142 */         setEnabled(true);
/* 1143 */         putValue("Name", HTMLEditor.this.undo.getRedoPresentationName());
/*      */       } else {
/* 1145 */         setEnabled(false);
/* 1146 */         putValue("Name", "Redo");
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public void insertActionPerformed()
/*      */   {
/* 1156 */     this.textPane.requestFocusInWindow();
/* 1157 */     JFileChooser jf = new JFileChooser();
/*      */
/*      */
/* 1160 */     int option = jf.showOpenDialog(this);
/*      */
/*      */
/* 1163 */     if (option == 0)
/*      */     {
/* 1165 */       File file = jf.getSelectedFile();
/* 1166 */       if (isImage(file))
/*      */       {
/* 1168 */         String imgsrc = file.getAbsolutePath();
/* 1169 */         String text = "<p><img src='file:" + imgsrc + "'></img></p>";
/* 1170 */         text = text + this.textPane.getText();
/* 1171 */         this.textPane.setText(text);
/*      */       }
/*      */
/*      */
/*      */     }
/*      */     else
/*      */     {
/* 1178 */       JOptionPane.showMessageDialog(this, "The file is not an image.", "Not Image", 0);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   private boolean isImage(File file)
/*      */   {
/* 1187 */     String name = file.getName();
/* 1188 */     return (name.endsWith(".jpg")) || (name.endsWith(".png")) || (name.endsWith(".jpeg")) || (name.endsWith(".gif"));
/*      */   }
/*      */
/*      */   class FrameListener
/*      */     extends java.awt.event.WindowAdapter
/*      */   {
/*      */     FrameListener() {}
/*      */
/*      */     public void windowClosing(java.awt.event.WindowEvent we) {}
/*      */   }
/*      */ }


/*
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */
