import java.io.File;
import java.io.IOException;
import java.text.*;
import java.util.*;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;

public class FormComponents extends JFrame implements ItemListener {
    private JFrame frame;

    private JLabel lblInfo = null;
    private JTextArea strResult = null;
    private JTextArea strInFile;
    private JButton btnInFile;
    //private JTable tblData;
    private DataTable tblData;
    private String sDefDir;

    VIniFiler ini = new VIniFiler("E:\\Java\\IdeaProjectsRosan\\TableExample\\IniFile.xml");

    private void addComponentsToPane(Container pane) {
        if (!(pane.getLayout() instanceof BorderLayout)) {
            pane.add(new JLabel("Container doesn't use BorderLayout!"));
            return;
        }

        lblInfo = new JLabel();
        strResult = new JTextArea("-------------------------------------------------");
        sDefDir = ini.IniReadValue("FILE","DefDir","None");
        if(sDefDir.compareTo("None") == 0) {
            sDefDir = System.getProperty("user.home");
        }
        // Top area: ------------------------------------------------------------------
        JPanel pnlTop = new JPanel();
        pnlTop.setLayout(new BorderLayout());
        pnlTop.setPreferredSize(new Dimension(300, 20));
        pane.add(pnlTop, BorderLayout.PAGE_START);

        strInFile = new JTextArea();
        pnlTop.add(strInFile, BorderLayout.CENTER);

        btnInFile = new JButton("...");
        btnInFile.setPreferredSize(new Dimension(20, 20));
        pnlTop.add(btnInFile, BorderLayout.LINE_END);
        btnInFile.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doInFile();
            }
        });
        // Data area: ------------------------------------------------------------------
        JTabbedPane pnlTabs = new JTabbedPane();

        //JPanel pnlData = new JPanel(false);
        tblData = new DataTable();
        tblData.strResult = this.strResult;
        tblData.setPreferredSize(new Dimension(700,400));
        //pnlData.setLayout(new GridLayout(1, 1));
        //pnlData.add(tblData);

        JScrollPane scrollPane = new JScrollPane(tblData);
        tblData.setFillsViewportHeight(true);

        pnlTabs.addTab("Data", null, scrollPane,"Does nothing");
        pnlTabs.setMnemonicAt(0, KeyEvent.VK_1);

        JPanel pnlLog = new JPanel(false);
        strResult.setEditable(false);
        strResult.setBackground(Color.black);
        strResult.setForeground(Color.white);
        strResult.setFont(new Font("Courier New", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(strResult,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        pnlLog.setLayout(new GridLayout(1, 1));
        pnlLog.add(scroll);
        ImageIcon icon = null; //createImageIcon("E:\\Java\\IdeaProjectsRosan\\TableExample\\image\\image\\Ring.gif","Red Bug");
        pnlTabs.addTab("Log", icon,  pnlLog,"Does nothing");
        pnlTabs.setMnemonicAt(1, KeyEvent.VK_2);

        pane.add(pnlTabs, BorderLayout.CENTER);
        // Bottom area: ----------------------------------------------------------------
        JPanel pnlBottom = new JPanel();
        pnlBottom.setLayout(new BorderLayout());
        pane.add(pnlBottom, BorderLayout.PAGE_END);

        lblInfo = new JLabel("");
        lblInfo.setBorder(BorderFactory.createLineBorder(Color.black));
        pnlBottom.add(lblInfo, BorderLayout.CENTER);

        JPanel pnlButtons = new JPanel();
        pnlButtons.setLayout(null);
        pnlButtons.setPreferredSize(new Dimension(210, 20));
        pnlBottom.add(pnlButtons, BorderLayout.LINE_END);

        int offBot = 2, wBotBut = 100, hBotBut = 20;

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBounds(offBot, 0, wBotBut, hBotBut);
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doCancel();
            }
        });
        pnlButtons.add(btnCancel);

        JButton btnCommit = new JButton("Commit");
        btnCommit.setBounds(offBot + wBotBut + 3, 0, wBotBut, hBotBut);
        btnCommit.addItemListener(this);
        btnCommit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doCommit();
            }
        });
        pnlButtons.add(btnCommit);
        //------------------------------------------------------------------------------
    }

    public void createAndShowGUI(){
        frame = new JFrame("Table example");
        //frame.setTitle("Text Format example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponentsToPane(frame.getContentPane());
        frame.pack();
        centerFrame();
        frame.setVisible(true);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

    }


    private void centerFrame() {
        Dimension windowSize = frame.getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();

        int dx = centerPoint.x - windowSize.width / 2;
        int dy = centerPoint.y - windowSize.height / 2;
        frame.setLocation(dx, dy);
    }

    private void WriteMsg(String sMes){
        if (lblInfo != null) lblInfo.setText(sMes);
        if (strResult != null) strResult.setText(strResult.getText() + "\n" + sMes);
    }

    private void doInFile(){
        int RC = -1;

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(sDefDir));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            strInFile.setText(selectedFile.getAbsolutePath());
            try {
                sDefDir = selectedFile.getCanonicalPath();
            } catch (IOException e) {
                WriteMsg("doInFile: " + e);
                return;
            }
            WriteMsg("InFile: " + strInFile.getText());
            RC = tblData.loadPersonalData(strInFile.getText());
            WriteMsg("tblData.loadPersonalData: " + RC);
        }
    }

    private void doCommit(){
        WriteMsg("Done: Commit");
    }

    private void doCancel(){
        WriteMsg("Done: Cancel");
    }
}
