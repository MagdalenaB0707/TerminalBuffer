package com.terminal;

public class Cell {

    private char character;

    private TerminalColor foreground;
    private TerminalColor background;

    private boolean bold;
    private boolean italic;
    private boolean underline;

    public Cell() {
        this.character = ' ';
        this.foreground = null;
        this.background = null;
        this.bold = false;
        this.italic = false;
        this.underline = false;
    }

    public Cell(Cell other) {
        this.character = other.character;
        this.foreground = other.foreground;
        this.background = other.background;
        this.bold = other.bold;
        this.italic = other.italic;
        this.underline = other.underline;
    }

    public char getCharacter() { return character; }
    public void setCharacter(char character) { this.character = character; }

    public TerminalColor getForeground() { return foreground; }
    public void setForeground(TerminalColor foreground) { this.foreground = foreground; }

    public TerminalColor getBackground() { return background; }
    public void setBackground(TerminalColor background) { this.background = background; }

    public boolean isBold() { return bold; }
    public void setBold(boolean bold) { this.bold = bold; }

    public boolean isItalic() { return italic; }
    public void setItalic(boolean italic) { this.italic = italic; }

    public boolean isUnderline() { return underline; }
    public void setUnderline(boolean underline) { this.underline = underline; }

    public void clear() {
        this.character = ' ';
        this.foreground = null;
        this.background = null;
        this.bold = false;
        this.italic = false;
        this.underline = false;
    }
}
