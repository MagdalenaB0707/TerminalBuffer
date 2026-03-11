package com.terminal;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class TerminalBuffer {

    private int width;
    private int height;

    private final int maxScrollback;

    private final List<Line> screen;

    private final Deque<Line> scrollback;

    private int cursorRow;
    private int cursorCol;

    private TerminalColor currentForeground;
    private TerminalColor currentBackground;
    private boolean currentBold;
    private boolean currentItalic;
    private boolean currentUnderline;

    public TerminalBuffer(int width, int height, int maxScrollback) {
        this.width = width;
        this.height = height;
        this.maxScrollback = maxScrollback;
        this.screen = new ArrayList<>();
        this.scrollback = new ArrayDeque<>();
        this.cursorRow = 0;
        this.cursorCol = 0;

        for (int i = 0; i < height; i++) {
            screen.add(new Line(width));
        }
    }

    public void setAttributes(TerminalColor foreground, TerminalColor background,
                              boolean bold, boolean italic, boolean underline) {
        this.currentForeground = foreground;
        this.currentBackground = background;
        this.currentBold = bold;
        this.currentItalic = italic;
        this.currentUnderline = underline;
    }

    public void resetAttributes() {
        this.currentForeground = null;
        this.currentBackground = null;
        this.currentBold = false;
        this.currentItalic = false;
        this.currentUnderline = false;
    }

    private void applyAttributes(Cell cell) {
        cell.setForeground(currentForeground);
        cell.setBackground(currentBackground);
        cell.setBold(currentBold);
        cell.setItalic(currentItalic);
        cell.setUnderline(currentUnderline);
    }

    public int getCursorRow() { return cursorRow; }
    public int getCursorCol() { return cursorCol; }

    public void setCursor(int row, int col) {
        this.cursorRow = Math.max(0, Math.min(row, height - 1));
        this.cursorCol = Math.max(0, Math.min(col, width - 1));
    }

    public void moveCursorUp(int n) {
        setCursor(cursorRow - n, cursorCol);
    }

    public void moveCursorDown(int n) {
        setCursor(cursorRow + n, cursorCol);
    }

    public void moveCursorLeft(int n) {
        setCursor(cursorRow, cursorCol - n);
    }

    public void moveCursorRight(int n) {
        setCursor(cursorRow, cursorCol + n);
    }

    public void writeText(String text) {
        for (char c : text.toCharArray()) {
            if (cursorCol >= width) {
                break;
            }
            Cell cell = screen.get(cursorRow).getCell(cursorCol);
            cell.setCharacter(c);
            applyAttributes(cell);
            cursorCol++;
        }
    }

    public void insertText(String text) {
        for (char c : text.toCharArray()) {
            if (cursorCol >= width) {
                cursorCol = 0;
                cursorRow++;
                if (cursorRow >= height) {
                    insertEmptyLineAtBottom();
                    cursorRow = height - 1;
                }
            }
            Line line = screen.get(cursorRow);
            for (int i = width - 1; i > cursorCol; i--) {
                Cell dest = line.getCell(i);
                Cell src = line.getCell(i - 1);
                dest.setCharacter(src.getCharacter());
                dest.setForeground(src.getForeground());
                dest.setBackground(src.getBackground());
                dest.setBold(src.isBold());
                dest.setItalic(src.isItalic());
                dest.setUnderline(src.isUnderline());
            }
            Cell cell = line.getCell(cursorCol);
            cell.setCharacter(c);
            applyAttributes(cell);
            cursorCol++;
        }
    }

    public void fillLine(char c) {
        Line line = screen.get(cursorRow);
        for (int i = 0; i < width; i++) {
            Cell cell = line.getCell(i);
            cell.setCharacter(c);
            applyAttributes(cell);
        }
    }

    public void clearLine() {
        screen.get(cursorRow).clear();
    }

    public void insertEmptyLineAtBottom() {
        Line topLine = screen.remove(0);
        scrollback.addLast(topLine);
        if (scrollback.size() > maxScrollback) {
            scrollback.removeFirst();
        }
        screen.add(new Line(width));
    }

    public void clearScreen() {
        for (Line line : screen) {
            line.clear();
        }
        cursorRow = 0;
        cursorCol = 0;
    }

    public void clearAll() {
        clearScreen();
        scrollback.clear();
    }

    public char getCharAt(int row, int col) {
        return getCell(row, col).getCharacter();
    }

    public Cell getAttributesAt(int row, int col) {
        return getCell(row, col);
    }

    public String getLine(int row) {
        if (row >= 0) {
            return screen.get(row).getText();
        } else {
            List<Line> scrollbackList = new ArrayList<>(scrollback);
            int index = scrollbackList.size() + row;
            if (index < 0 || index >= scrollbackList.size()) {
                throw new IndexOutOfBoundsException("Scrollback row " + row + " out of bounds");
            }
            return scrollbackList.get(index).getText();
        }
    }

    public String getScreenContent() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < screen.size(); i++) {
            sb.append(screen.get(i).getText());
            if (i < screen.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public String getAllContent() {
        StringBuilder sb = new StringBuilder();
        for (Line line : scrollback) {
            sb.append(line.getText()).append("\n");
        }
        sb.append(getScreenContent());
        return sb.toString();
    }

    private Cell getCell(int row, int col) {
        if (row < 0 || row >= height) {
            throw new IndexOutOfBoundsException("Row " + row + " out of bounds");
        }
        if (col < 0 || col >= width) {
            throw new IndexOutOfBoundsException("Column " + col + " out of bounds");
        }
        return screen.get(row).getCell(col);
    }


    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getScrollbackSize() { return scrollback.size(); }
}
