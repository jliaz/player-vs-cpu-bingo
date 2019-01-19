package bingogui;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;

public class BingoGUI {

    private static Timer timer = new Timer(3000, new TimerAction()); //declaring timer 				
    private static JFrame frame = new JFrame("Bingo GUI"); //GLOBAL VARIABLES
    private static JPanel cpuPanelOne = new JPanel(new GridLayout(6, 5));
    private static JPanel cpuPanelTwo = new JPanel(new GridLayout(6, 5));
    private static JPanel playerPanelOne = new JPanel(new GridLayout(6, 5));
    private static JPanel playerPanelTwo = new JPanel(new GridLayout(6, 5));
    private static JLabel[][] callBoard = callBoardNumbers();
    private static String callNumber = randomCall(callBoard);
    private static JLabel randomCall = new JLabel();
    private static JLabel[][] cpuCardLabels1;
    private static JLabel[][] cpuCardLabels2;
    private static JLabel[][] playerCardLabels1;
    private static JLabel[][] playerCardLabels2;
    private static JButton[][] playerCardButtons1 = new JButton[6][5];
    private static JButton[][] playerCardButtons2 = new JButton[6][5];
    private static JButton[][] cpuCardButtons = new JButton[6][5];
    private static JButton source = new JButton(); //clicked button source
    private static boolean vWin = false; // win conditions 
    private static boolean hWin = false;
    private static boolean dWin1 = false;
    private static boolean dWin2 = false;
    private static int winRow; //index of winning row
    private static int winCol; // index of winning col
    private static JPanel mainPanel = new JPanel(); //PANELS
    private static JPanel callBoardPanel = new JPanel(new GridLayout(5, 16));
    private static JPanel cpuCardsPanel = new JPanel();
    private static JPanel playerCardsPanel = new JPanel();

    public static void restart() {
        playerPanelOne.removeAll();
        playerPanelTwo.removeAll();
        callBoardPanel.removeAll();
        cpuPanelOne.removeAll();
        cpuPanelTwo.removeAll();
        randomCall.setText("Get ready to play BINGO!");
        randomCall.setForeground(Color.MAGENTA);
        randomCall.setFont(new Font("Arial", Font.BOLD, 24));

        callNumber = randomCall(callBoard);
        cpuCardLabels1 = createCards();
        cpuCardLabels2 = createCards();
        playerCardLabels1 = createCards();
        playerCardLabels2 = createCards();

        menu(); //THESE ARE PRINTED ONLY ONCE
        displayCallBoard(callBoardPanel, callBoard, callNumber);
        displayCards(cpuCardLabels1, cpuCardButtons, cpuPanelOne, "#2d77ef");
        displayCards(cpuCardLabels2, cpuCardButtons, cpuPanelTwo, "#2d77ef");
        displayCards(playerCardLabels1, playerCardButtons1, playerPanelOne, "#ef2d33");
        displayCards(playerCardLabels2, playerCardButtons2, playerPanelTwo, "#ef2d33");

        cpuPanelOne.setPreferredSize(new Dimension(240, 300)); //APPEARANCE AND DIMENSIONS
        cpuPanelTwo.setPreferredSize(new Dimension(240, 300));
        playerPanelOne.setPreferredSize(new Dimension(240, 300));
        playerPanelTwo.setPreferredSize(new Dimension(240, 300));
        cpuCardsPanel.setPreferredSize(new Dimension(500, 300));
        playerCardsPanel.setPreferredSize(new Dimension(500, 300));
        callBoardPanel.setPreferredSize(new Dimension(1000, 250));

        cpuCardsPanel.setLayout(new BorderLayout());
        cpuCardsPanel.add(cpuPanelOne, BorderLayout.WEST);
        cpuCardsPanel.add(cpuPanelTwo, BorderLayout.EAST);
        cpuCardsPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.BLACK, 3), "CPU BOARD", TitledBorder.LEFT, TitledBorder.CENTER, new Font("Arial", Font.BOLD, 24), Color.decode("#2d77ef")));
        playerCardsPanel.setLayout(new BorderLayout());
        playerCardsPanel.add(playerPanelOne, BorderLayout.WEST);
        playerCardsPanel.add(playerPanelTwo, BorderLayout.EAST);
        playerCardsPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.BLACK, 3), "PLAYER BOARD", TitledBorder.LEFT, TitledBorder.CENTER, new Font("Arial", Font.BOLD, 24), Color.decode("#ef2d33")));

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(callBoardPanel, BorderLayout.NORTH);
        mainPanel.add(cpuCardsPanel, BorderLayout.WEST);
        mainPanel.add(playerCardsPanel, BorderLayout.EAST);
        mainPanel.add(randomCall, BorderLayout.SOUTH);

        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(mainPanel);
        frame.pack();

        timer.start(); //initializing timer
    }

    public static void menu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem[] menuItem = new JMenuItem[3];
        char[] itemLetter = {'N', 'B', 'E'};
        String[] itemName = {"New Game", "Call Bingo", "Exit"};

        for (int i = 0; i < menuItem.length; i++) {
            menuItem[i] = new JMenuItem(itemName[i]);
            menuItem[i].setActionCommand(i == 1 ? "B" : itemName[i].substring(0, 1));
            menuItem[i].addActionListener(new MenuAction());
            file.setMnemonic(KeyEvent.VK_F);
            file.add(menuItem[i]);
            if (i == 1) {
                file.addSeparator();
            }
        }

        menuItem[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        menuItem[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));
        menuItem[2].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));

        /*	JMenuItem newGame = new JMenuItem("New Game");
		JMenuItem callBingo = new JMenuItem("Call BINGO");
		JMenuItem exit = new JMenuItem("Exit");
	
		newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		callBingo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
     
        newGame.addActionListener(new MenuAction());
        callBingo.addActionListener(new MenuAction());
        exit.addActionListener(new MenuAction());
        newGame.setActionCommand("N");
        callBingo.setActionCommand("B");
        exit.setActionCommand("E");
        
		file.setMnemonic(KeyEvent.VK_F);
		file.add(newGame);
		file.add(callBingo);
		file.addSeparator();
		file.add(exit); */
        menuBar.add(file);
        frame.setJMenuBar(menuBar);
    }

    public static JLabel[][] callBoardNumbers() { //method for  initializng call board numbers
        JLabel[][] callBoard = new JLabel[5][16];
        char[] bingo = {'B', 'I', 'N', 'G', 'O'};

        for (int row = 0; row < callBoard.length; row++) {
            for (int col = 0; col < callBoard[row].length; col++) {
                callBoard[row][col] = (col == 0) ? new JLabel(Character.toString(bingo[row]), SwingConstants.CENTER) : new JLabel(Integer.toString(row * 15 + col), SwingConstants.CENTER);
                callBoard[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            }
        }
        return callBoard;
    }

    public static void displayCallBoard(JPanel panel, JLabel[][] callBoard, String randomCall) {
        String[] bingo = {"B", "I", "N", "G", "O"};
        for (int row = 0; row < callBoard.length; row++) {
            for (int col = 0; col < callBoard[row].length; col++) {
                callBoard[row][col].setPreferredSize(new Dimension(50, 50));
                callBoard[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                callBoard[row][col].setBackground(Color.WHITE);
                callBoard[row][col].setForeground(Color.decode(col == 0 ? "#c877d8" : "#2d77ef"));
                callBoard[row][col].setFont(new Font("Arial", Font.BOLD, (col == 0 ? 20 : 14)));
                panel.add(callBoard[row][col]);
            }
        }
        TitledBorder callBoardBorder = new TitledBorder(BorderFactory.createLineBorder(Color.BLACK, 3), "CALL BOARD", TitledBorder.LEFT, TitledBorder.CENTER, new Font("Arial", Font.BOLD, 24), Color.decode("#77d88d"));
        panel.setBorder(callBoardBorder);
    }

    public static void editCallBoard(JLabel[][] callBoardLabel, String randomCall) { //for updating callboard with *
        for (int row = 0; row < callBoardLabel.length; row++) {
            for (int col = 0; col < callBoardLabel[row].length; col++) {
                if (callBoardLabel[row][col].getText().equals(randomCall.substring(1))) {
                    callBoardLabel[row][col].setOpaque(true);
                    callBoardLabel[row][col].setBackground(Color.decode("#77d88d"));
                }
            }
        }
    }

    public static JLabel[][] createCards() { //initializes player and CPU card numbers
        JLabel[][] gameCard = new JLabel[6][5];
        Random rand = new Random();
        ArrayList<Integer> numbers = new ArrayList<Integer>(75);
        int choice;
        char[] bingo = {'B', 'I', 'N', 'G', 'O'};

        for (int i = 0; i < 75; i++) {
            numbers.add(new Integer(i + 1));
        }
        for (int row = 0; row < gameCard.length; row++) {
            for (int col = 0; col < gameCard[row].length; col++) {
                do {
                    choice = rand.nextInt(15) + col * 15 + 1;
                } while (numbers.indexOf(choice) == -1);
                numbers.remove(numbers.indexOf(choice)); //(explaining code below: if row is 0, spell bingo, else fill with numbers
                gameCard[row][col] = (row == 0) ? new JLabel(Character.toString(bingo[col]), SwingConstants.CENTER) : new JLabel(Integer.toString(choice), SwingConstants.CENTER);
                gameCard[row][col].setFont(new Font("Arial", Font.BOLD, row == 0 ? 20 : 14));
            }
        }
        return gameCard;
    }

    public static void displayCards(JLabel[][] cardLabel, JButton[][] cardButton, JPanel panel, String color) { //displays player and CPU cards
        for (int row = 0; row < cardLabel.length; row++) {
            for (int col = 0; col < cardLabel[row].length; col++) {
                cardButton[row][col] = new JButton(cardLabel[row][col].getText());
                cardLabel[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cardButton[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cardButton[row][col].setFont(new Font("Arial", Font.BOLD, 14));
                cardLabel[row][col].setForeground(Color.decode(row == 3 && col == 2 ? "#999999" : "#2d77ef")); //if free space, set text colour to grey
                if (row == 3 && col == 2 || color.equals("#2d77ef") || row == 0) { //adding labels
                    cardLabel[3][2].setOpaque(true);
                    cardLabel[3][2].setBackground(Color.decode(color));
                    cardLabel[0][col].setForeground(Color.decode("#c877d8"));
                    panel.add(cardLabel[row][col]);
                } else { //adding buttons
                    cardButton[row][col].setForeground(Color.decode("#ef2d33"));
                    cardButton[row][col].addActionListener(new ButtonAction());
                    panel.add(cardButton[row][col]);
                }
            }
        }
    }

    public static void editCPUCards(JLabel[][] cardLabel, String callNumber) { //updates player and CPU cards when random call is made
        for (int row = 0; row < cardLabel.length; row++) {
            for (int col = 0; col < cardLabel[row].length; col++) {
                if (cardLabel[row][col].getText().equals(callNumber.substring(1))) {
                    cardLabel[row][col].setOpaque(true);
                    cardLabel[row][col].setBackground(Color.decode("#2d77ef"));
                    cardLabel[row][col].setForeground(Color.decode("#999999"));
                }
            }
        }
    }

    public static void editPlayerCards(JLabel[][] card, JButton source) { //updates player label card when button is pressed (this is used later for checking win)
        for (int row = 0; row < card.length; row++) {
            for (int col = 0; col < card[row].length; col++) {
                if (card[row][col].getText().equals(source.getText())) {
                    card[row][col].setOpaque(true);
                }
            }
        }
    }

    public static String randomCall(JLabel[][] callBoard) { //generates random call
        Random rand = new Random();
        String callNumber = "";
        char bingo[] = {'B', 'I', 'N', 'G', 'O'}; // added another O for if choice == 75, index == 5	
        do {
            int choice = rand.nextInt(75) + 1;
            int index = (choice - 1) / 15; //for below code: if number has not been already called, the random call is respective letter + call number
            callNumber = (callBoard[index][choice - index * 15].getBackground() == Color.GREEN) ? "" : "" + bingo[index] + choice;
        } while (callNumber.equals("")); //if number has already been called, loop continues to iterate until a new number is randomly selected
        return callNumber;
    }

    public static boolean daubed(JLabel card) { //method for checking if card is daubed
        return card.isOpaque() == true;
    }

    public static boolean checkWin(JLabel[][] card) {
        dWin1 = daubed(card[1][0]) && daubed(card[2][1]) && daubed(card[4][3]) && daubed(card[5][4]);
        dWin2 = daubed(card[1][4]) && daubed(card[2][3]) && daubed(card[4][1]) && daubed(card[5][0]);

        for (int row = 1; row < 6; row++) { //checking horizontal wins
            hWin = daubed(card[row][0]) && daubed(card[row][1]) && daubed(card[row][2]) && daubed(card[row][3]) && daubed(card[row][4]);
            if (hWin) {
                winRow = row;
                break;
            }
        }
        for (int col = 0; col < 5; col++) { //checking vertical wins
            vWin = daubed(card[1][col]) && daubed(card[2][col]) && daubed(card[3][col]) && daubed(card[4][col]) && daubed(card[5][col]);
            if (vWin) {
                winCol = col;
                break;
            }
        }
        return vWin || dWin1 || dWin2 || hWin;
    }

    public static void highlightWin(JLabel[][] cardLabel, JButton[][] cardButton, String callNumber, boolean highlight, String winner) {
        for (int row = 1; row < cardLabel.length; row++) {
            for (int col = 0; col < cardLabel[row].length; col++) {
                if (checkWin(cardLabel) && dWin1 && row - col == 1 && highlight) {
                    cardLabel[row][col].setBackground(Color.decode("#42f48c"));
                    cardButton[row][col].setBackground(Color.decode("#42f48c"));
                } else if (checkWin(cardLabel) && dWin2 && row + col == 5 && highlight) {
                    cardLabel[row][col].setBackground(Color.decode("#42f48c"));
                    cardButton[row][col].setBackground(Color.decode("#42f48c"));
                } else if (checkWin(cardLabel) && hWin && row == winRow && highlight) {
                    cardLabel[row][col].setBackground(Color.decode("#42f48c"));
                    cardButton[row][col].setBackground(Color.decode("#42f48c"));
                } else if (checkWin(cardLabel) && vWin && col == winCol && highlight) {
                    cardLabel[row][col].setBackground(Color.decode("#42f48c"));
                    cardButton[row][col].setBackground(Color.decode("#42f48c"));
                }
                cardLabel[row][col].setForeground(Color.decode("#999999"));
                cardButton[row][col].setEnabled(false);
            }
        }
        if (vWin && highlight) {
            JOptionPane.showMessageDialog(frame, "The " + winner + " has won the game! (Vertical Bingo)");
        }
        if (hWin && highlight) { // not else if because these conditions are not mutually exclusive
            JOptionPane.showMessageDialog(frame, "The " + winner + " has won the game! (Horizontal Bingo)");
        }
        if ((dWin1 || dWin2) && highlight) {
            JOptionPane.showMessageDialog(frame, "The " + winner + " has won the game! (Diagonal Bingo)");
        }
    }

    public static void endGame(boolean highlight) { // ending game : checking for wins, highlighting, and pop-ups
        timer.stop();
        highlightWin(playerCardLabels2, playerCardButtons2, callNumber, highlight, "player");
        highlightWin(playerCardLabels1, playerCardButtons1, callNumber, highlight, "player");
        highlightWin(cpuCardLabels1, cpuCardButtons, callNumber, !highlight, "CPU");
        highlightWin(cpuCardLabels2, cpuCardButtons, callNumber, !highlight, "CPU");
        randomCall.setText("Select New Game from the menu to play again");
    }

    public static void main(String[] args) {

        restart();
    }

    private static class ButtonAction implements ActionListener { //action listener for player board buttons

        public void actionPerformed(ActionEvent event) {
            source = (JButton) event.getSource();
            if (!source.getText().equals(callNumber.substring(1))) {
                timer.stop();
                endGame(false);
                randomCall.setText("Select New Game from the menu to play again");
                JOptionPane.showMessageDialog(frame, "False Daub! You have lost the game!"); //pop-up message
            } else {
                source.setBackground(Color.decode("#ef2d33"));
                source.setEnabled(false);
                editPlayerCards(playerCardLabels1, source);
                editPlayerCards(playerCardLabels2, source);
            }
        }
    }

    private static class MenuAction implements ActionListener { //action listener for menu

        public void actionPerformed(ActionEvent event) {
            String command = event.getActionCommand();
            if (command.equals("N")) {
                timer.stop();
                restart();
            } else if (command.equals("B")) {
                endGame(true);
                if (!(checkWin(playerCardLabels1) || checkWin(playerCardLabels2))) {
                    JOptionPane.showMessageDialog(frame, "False BINGO call! You have lost the game!");
                }
            } else {
                System.exit(0);
            }
        }
    }

    private static class TimerAction implements ActionListener { //action listener for timer

        public void actionPerformed(ActionEvent event) {
            callNumber = randomCall(callBoard);
            randomCall.setText("The current call is: " + callNumber);
            /*Voice voice = VoiceManager.getInstance().getVoice("kevin16");
			voice.allocate();
			voice.speak(callNumber);*/
            editCallBoard(callBoard, callNumber);
            editCPUCards(cpuCardLabels1, callNumber);
            editCPUCards(cpuCardLabels2, callNumber);
            if (checkWin(cpuCardLabels1) || checkWin(cpuCardLabels2)) {
                endGame(false);
            }
        }
    }
}
