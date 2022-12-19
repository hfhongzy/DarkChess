package gameComponent;

import buttonComponent.GameButton;
import model.PanelType;
import view.MainFrame;
import view.PlayGamePanel;

import javax.swing.*;

public class OptionalBox extends JComponent {
    static final int WIDTH = 300;
    static final int HEIGHT = 400;
    Chessboard chessboard;
    GameButton undoButton, redoButton, cheatButton, saveButton, loadButton, restartButton, returnButton;
    boolean enable_undo_redo, enable_cheat, enable_restart, enable_load_save;
    public OptionalBox(PlayGamePanel playGamePanel, Chessboard chessboard) {
        setSize(WIDTH, HEIGHT);

        this.chessboard = chessboard;
        undoButton = new GameButton(70, 40, "撤销", chessboard::undo);
        undoButton.setLocation(30, 40);
        add(undoButton);
        redoButton = new GameButton(70, 40, "重做", chessboard::redo);
        redoButton.setLocation(150, 40);
        add(redoButton);
        cheatButton = new GameButton(70, 40, "透视", chessboard::switchCheating);
        cheatButton.setLocation(30, 90);
        add(cheatButton);
        saveButton = new GameButton(70, 40, "存档", playGamePanel::save); //playGamePanel::save
        saveButton.setLocation(30, 140);
        add(saveButton);
        loadButton = new GameButton(70, 40, "读档", playGamePanel::load); //
        loadButton.setLocation(150, 140);
        add(loadButton);
        restartButton = new GameButton(70, 40, "重开", () -> {
            if (JOptionPane.showConfirmDialog(null, "你真的要重开吗？", null, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                playGamePanel.restart();
            }
        });
        restartButton.setLocation(150, 190);
        add(restartButton);
    
        returnButton = new GameButton(70, 40, "结束", () -> {
            if (JOptionPane.showConfirmDialog(null, "你真的要结束游戏吗？", null, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                playGamePanel.returnToMain();
            }
        });
        returnButton.setLocation(30, 190);
        add(returnButton);
    }
    public void start(boolean undo_redo, boolean cheat, boolean restart, boolean load) {
        enable_undo_redo = undo_redo;
        enable_cheat = cheat;
        undoButton.setWorking(false);
        redoButton.setWorking(false);
        cheatButton.setWorking(cheat);
        restartButton.setWorking(restart);
        loadButton.setWorking(load);
        saveButton.setWorking(load); //new
    }
}
