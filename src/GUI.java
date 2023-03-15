import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;


/** GUI - contain "main" method and show GUI
 */
public abstract class GUI extends Component implements ActionListener {
    final static boolean shouldFill = true;
    final static boolean RIGHT_TO_LEFT = false;
    static JFileChooser fc = new JFileChooser();

    static JFileChooser fcOutput = new JFileChooser();

    static JButton buttonInput;
    static JTextArea log;

    static JScrollPane scrollPane;

    static String inputPath = null;
    static String outputPath = null;



    public static void addComponentsToPane(Container pane) throws IOException {
        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }

        JButton button, buttonOutput, buttonRun;
        JLabel label1, label2, label3, label4, label5, label6;
        ImageIcon icon = createImageIcon("/images/subra.png",
                "picture");


        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        if (shouldFill) {
            c.fill = GridBagConstraints.HORIZONTAL;
        }


        label1 = new JLabel("Формат на данни в EXCEL от Атлас:");
        c.fill = GridBagConstraints.RELATIVE;
        c.gridwidth = 0;
        c.gridy = 0;
        pane.add(label1, c);


        label2 = new JLabel(icon);
        c.fill = GridBagConstraints.RELATIVE;
        c.gridwidth = 0;
        c.gridy = 1;
        pane.add(label2, c);

        label3 = new JLabel(" Директория(папка) за четене: ");
        c.fill = GridBagConstraints.REMAINDER;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 2;
        pane.add(label3, c);



        label4 = new JLabel("    Не е избрана!");
        c.fill = GridBagConstraints.REMAINDER;
        c.gridwidth = 2;
        c.gridx = 1;
        c.gridy = 2;
        pane.add(label4, c);


        buttonInput = new JButton("Избор на директория за четене на XLXS");

        c.fill = GridBagConstraints.REMAINDER;
        c.gridwidth = 3;
        c.gridx = 3;
        c.gridy = 2;
        pane.add(buttonInput, c);

        // Set input folder
        buttonInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){


                fc.setDialogTitle("Избери директория за четене на XLSX");
                fc.setToolTipText("Избери XLXS файл!");
                fc.setApproveButtonText("Избери директория");
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                fc.showOpenDialog(null);


                if (JFileChooser.APPROVE_OPTION==0) {
                    java.io.File f = fc.getSelectedFile();

                    label4.setText(f.getPath());
                    inputPath = f.getPath();


                }

            }
        });



        label5 = new JLabel("Директория(папка) за запис: ");
        c.fill = GridBagConstraints.REMAINDER;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 3;
        pane.add(label5, c);


        label6 = new JLabel("   Не е избрана!");
        c.fill = GridBagConstraints.REMAINDER;
        c.gridwidth = 2;
        c.gridx = 1;
        c.gridy = 3;
        pane.add(label6, c);


        buttonOutput = new JButton("Избор на директория за запис на S$$");

        c.fill = GridBagConstraints.REMAINDER;
        c.gridwidth = 3;
        c.gridx = 3;  
        c.gridy = 3;  
        pane.add(buttonOutput, c);


        // Set output folder
        buttonOutput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){

                fcOutput.setDialogTitle("Избери директория за запис на S$$");

                fcOutput.setApproveButtonText("Маркирай папка");

                fcOutput.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);


                fcOutput.showOpenDialog(null);

                if (JFileChooser.APPROVE_OPTION==0) {
                    java.io.File fOut = fcOutput.getSelectedFile();

                    label6.setText(fOut.getPath());
                    outputPath = fOut.getPath();
                }
            }
        });


        buttonRun = new JButton(" Генерирай файлове (S$$) ");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.PAGE_END;
        c.insets = new Insets(10, 0, 0, 0);
        c.gridx = 1;
        c.gridwidth = 4;
        c.gridy = 5;
        pane.add(buttonRun, c);

        // Invoke ConvertData - start Convert
        buttonRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ConvertData.Convert(inputPath, outputPath);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });



        log = new JTextArea(15,6);



        log.setEditable(false);
        scrollPane = new JScrollPane(log);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 6.0;
        c.gridx = 0;
        c.gridy = 7;
        pane.add(scrollPane, c);



        // Redirect console to JTextArea
        PrintStream printStream = new PrintStream(new OutputStream() {

            @Override
            public void write(int b) throws IOException {
                String test = String.valueOf((char) b);
                log.append(test);
                log.setCaretPosition(log.getDocument().getLength());
            }


        });

        System.setOut(printStream);
        System.setErr(printStream);


    }


    /**
     * Returns an ImageIcon, or null if the path was invalid.
     */
    protected static ImageIcon createImageIcon(String path,
                                               String description) {
        java.net.URL imgURL = GUI.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }


    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() throws IOException {
//Create and set up the window.
        JFrame frame = new JFrame("Конвертиране от Ексел към S$$");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



//Set up the content pane.
        addComponentsToPane(frame.getContentPane());

//Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
//Schedule a job for the event-dispatching thread:
//creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowGUI();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


}
