import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

public class TicTacToe implements ActionListener{

    int increment = 1;
    int total_plays = 0;
    private static final int ROW_TYPE = 0;
    private static final int COLUMN_TYPE = 1;
    private static final int DIAGONAL_TYPE = 2;
    private static final int CROSSES = 0;
    private static final int LEFT_DIAGONAL = 0;
    private static final int RIGHT_DIAGONAL = 1;
    private static final int NOUGHTS = 1;
    private static final int BOARD_SIZE = 3;

    // Setting up the GUI
    Random random = new Random();
    JFrame frame = new JFrame();
    JPanel title_panel = new JPanel();
    JPanel button_panel = new JPanel();
    JLabel textfield = new JLabel();
    JButton[][] buttons = new JButton[BOARD_SIZE][BOARD_SIZE];
    boolean player1_turn;
    int computer_type;
    int player_type;

    TicTacToe(){
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,800);
        frame.getContentPane().setBackground(new Color(50, 50, 50));
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);

        textfield.setBackground(new Color(25,25,25));
        textfield.setForeground(new Color(25, 255, 0));
        textfield.setFont(new Font("Arial", Font.BOLD, 75));
        textfield.setHorizontalAlignment(JLabel.CENTER);
        textfield.setText("Tic Tac Toe");
        textfield.setOpaque(true);

        title_panel.setLayout(new BorderLayout());
        title_panel.setBounds(0,0,800, 100);
        button_panel.setLayout(new GridLayout(BOARD_SIZE,BOARD_SIZE));
        button_panel.setBackground(new Color(150,150,150));

        for (int i=0; i<BOARD_SIZE; i++){
            for (int j=0; j<BOARD_SIZE; j++){
                buttons[i][j] = new JButton();
                button_panel.add(buttons[i][j]);
                buttons[i][j].setFont(new Font("Arial",Font.BOLD, 120));
                buttons[i][j].setFocusable(false);
                buttons[i][j].addActionListener(this);
            }
        }

        title_panel.add(textfield);
        frame.add(title_panel, BorderLayout.NORTH);
        frame.add(button_panel);
        firstTurn();
    }

  
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j=0; j<BOARD_SIZE; j++) {
                if (e.getSource() == buttons[i][j]) {
                    if (player1_turn) {
                        // updates board after every player move
                        if (buttons[i][j].getText().equals("")) {
                            buttons[i][j].setForeground(new Color(255, 0, 0));
                            buttons[i][j].setText("X");
                            player1_turn = false;
                            textfield.setText("O Turn");
                            int[] position = new int[6];
                            if (evaluate(buttons, position) == 10){
                                win(buttons, position, CROSSES);
                            }
                            // makes computer play every even move
                            if (total_plays % 2 == 0 && total_plays != 8) {
                                makeMove(buttons, computer_type, findBestMove(buttons, computer_type));
                                total_plays++;
                            }
                        }
                    } else {
                        buttons[i][j].setForeground(new Color(0, 0, 255));
                        buttons[i][j].setText("O");
                        player1_turn = true;
                        textfield.setText("X Turn");
                        int[] position = new int[6];
                        if(evaluate(buttons, position) == -10){
                            win(buttons, position, NOUGHTS);
                        }
                        // makes computer play every even move
                        if (total_plays % 2 == 0 && total_plays != 8) {
                            makeMove(buttons, computer_type, findBestMove(buttons, computer_type));
                            total_plays++;
                        }
                    }
                }
            }
        }
        if (isTerminal(buttons) == 0) {
            textfield.setText("Tie. Game Over");
        }
        total_plays += increment;
    }

    // Adjusts physical board for each computer move after 1.5s.
    public void makeMove(JButton[][] buttons, int computer_type, int[] move_position){
        ActionListener taskPerformer = ae -> {
            if (computer_type==0){
                buttons[move_position[0]][move_position[1]].setForeground(new Color(255, 0, 0));
                buttons[move_position[0]][move_position[1]].setText("O");
                textfield.setText("X Turn");
                player1_turn = true;
                int[] position = new int[7];
                if (evaluate(buttons, position) == -10){
                    win(buttons, position, NOUGHTS);
                }
            }
             else {
                buttons[move_position[0]][move_position[1]].setForeground(new Color(255, 0, 0));
                buttons[move_position[0]][move_position[1]].setText("X");
                textfield.setText("O Turn");
                player1_turn = false;
                int[] position = new int[7];
                if (evaluate(buttons, position) == 10){
                    win(buttons, position, CROSSES);
                }
            }
        };
        Timer timer = new Timer(1000, taskPerformer);
        timer.setRepeats(false);
        timer.start();
    }

    // Randomises whether noughts or crosses will go first.
    public void firstTurn(){

        if(random.nextInt(2) == 0){
            player1_turn = true;
            player_type = 1;
            computer_type = 0;
            textfield.setText("X turn");
        }
        else {
            player1_turn = false;
            player_type = 0;
            computer_type = 1;
            textfield.setText("O Turn");
        }
    }

    // method that calculates board cost for the current board (10 if crosses wins, -10 if noughts wins) 
    static int evaluate(JButton[][] buttons, int[] matches) {
        int score=0;
        // check rows
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (buttons[i][0].getText().equals(buttons[i][1].getText()) && buttons[i][1].getText().equals(buttons[i][2].getText())) {
                matches[0] = i;
                matches[1] = ROW_TYPE;
                if (buttons[i][0].getText().equals("X")) {
                    score = 10;
                } else if ((buttons[i][0].getText().equals("O"))){
                    score = -10;
                }
            }
        }

        // check diagonals
         if (buttons[0][0].getText().equals(buttons[1][1].getText()) && buttons[1][1].getText().equals(buttons[2][2].getText())){
             matches[2] = LEFT_DIAGONAL;
             matches[1] = DIAGONAL_TYPE;
            if (buttons[1][1].getText().equals("X")){
                score = 10;
            }
            if (buttons[1][1].getText().equals("O")){
                score = -10;
            }
         }

        if (buttons[0][2].getText().equals(buttons[1][1].getText()) && buttons[1][1].getText().equals(buttons[2][0].getText())){
            matches[2] = RIGHT_DIAGONAL;
            matches[1] = DIAGONAL_TYPE;
            if (buttons[1][1].getText().equals("X")){
                score = 10;
            } if (buttons[1][1].getText().equals("O")){
                score = -10;
            }
        }

        // check columns
        for (int j = 0; j < BOARD_SIZE; j++) {
            if (buttons[0][j].getText().equals(buttons[1][j].getText()) && buttons[1][j].getText().equals(buttons[2][j].getText())) {
                matches[0] = j;
                matches[1] = COLUMN_TYPE;
                if (buttons[0][j].getText().equals("X")) {
                    score = 10;
                } else if ((buttons[0][j].getText().equals("O"))){
                    score = -10;
                }
            }
        }
        return score;
    }

    public int minimax(JButton[][] buttons, int depth, int player) {
        int[] temp = new int[7];
        int score = evaluate(buttons, temp);
        // Returns the score if the board ends with victory or loss (bottom of the tree)
        if (score==10){
            return score;
        }
        if (score==-10){
            return score;
        }
        int best_val;

        // Returns 0 if the board ends with a tie (bottom of the tree)
        if (isTerminal(buttons) == 0) {
            return 0;
        }
        if (player==1) {
            best_val = -1000;
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j=0; j<BOARD_SIZE; j++){
                    if (Objects.equals(buttons[i][j].getText(), "")) {
                        buttons[i][j].setText("X");
                        best_val = Math.max(best_val, minimax(buttons, depth + 1, 0));
                        buttons[i][j].setText("");
                    }
                }
            }
        } else {
            best_val = 1000;
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (Objects.equals(buttons[i][j].getText(), "")) {
                        buttons[i][j].setText("O");
                        best_val = Math.min(best_val, minimax(buttons, depth + 1, 1));
                        buttons[i][j].setText("");
                    }
                }
            }
        }
        return best_val;
    }

    // Function that returns 0 if there are no spaces left on the board.
    public int isTerminal(JButton[][] buttons){
        for (int i=0; i<BOARD_SIZE; i++){
            for (int j=0; j<BOARD_SIZE; j++){
                if (buttons[i][j].getText().equals("")) {
                    return(1);
                }
            }
        }
        return(0);
    }

    /* Method that returns move which results in best board score, depending on whether the 
    current player is minimising or maximising (minimax) */
    public int[] findBestMove(JButton[][] buttons, int player){
        int bestVal;
        if (player==1){
            bestVal = -1000;
        }
        else {
            bestVal = 1000;
        }
        int[] best_move_position = new int[2];

        for (int i=0; i<BOARD_SIZE; i++){
            for (int j=0; j<BOARD_SIZE; j++){
                if (buttons[i][j].getText().equals("")){
                    if (player == 1){
                        buttons[i][j].setText("X");
                        int moveVal = minimax(buttons, 0, 0);
                        buttons[i][j].setText("");
                        if (moveVal > bestVal){
                            bestVal = moveVal;
                            best_move_position[0] = i;
                            best_move_position[1] = j;
                        }
                    }
                    else {
                        buttons[i][j].setText("O");
                        int moveVal = minimax(buttons, 0, 1);
                        buttons[i][j].setText("");
                        if (moveVal < bestVal){
                            bestVal = moveVal;
                            best_move_position[0] = i;
                            best_move_position[1] = j;
                        }
                    }
                }
            }

        }
        return best_move_position;
    }

    // Ensures players can not interact with game once it has ended.
    public void endBoard(JButton[][] buttons){
        for (int i=0; i<BOARD_SIZE; i++){
            for (int j=0; j<BOARD_SIZE; j++){
                buttons[i][j].setEnabled(false);
            }
        }
    }

    // Adjusts the board in the case of victory.
    public void win(JButton[][] buttons, int[] position, int player){
        if (position[1] == ROW_TYPE){
            for (int i=0; i<BOARD_SIZE; i++){
                buttons[position[0]][i].setBackground(Color.GREEN);
            }
        } else if (position[1] == COLUMN_TYPE){
            for (int i=0; i<BOARD_SIZE; i++){
                buttons[i][position[0]].setBackground(Color.GREEN);
            }
        }
        else if (position[1] == DIAGONAL_TYPE){
            buttons[1][1].setBackground(Color.GREEN);
            if (position[2] == LEFT_DIAGONAL){
                buttons[0][0].setBackground(Color.GREEN);
                buttons[2][2].setBackground(Color.GREEN);
            }
            else if (position[2] == RIGHT_DIAGONAL){
                buttons[0][2].setBackground(Color.GREEN);
                buttons[2][0].setBackground(Color.GREEN);
            }
        }
        endBoard(buttons);
        if (player == CROSSES){
            textfield.setText("X wins");
        }
        else {
            textfield.setText("O wins");
        }
    }
}
