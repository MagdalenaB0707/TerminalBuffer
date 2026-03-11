package com.terminal;

public class Line {

    private final Cell[] cells;
    private final int width;

    public Line(int width) {
        this.width = width;
        this.cells = new Cell[width];
        for (int i = 0; i < width; i++) {
            this.cells[i] = new Cell();
        }
    }

    public Line(Line other) {
        this.width = other.width;
        this.cells = new Cell[width];
        for (int i = 0; i < width; i++) {
            this.cells[i] = new Cell(other.cells[i]);
        }
    }

    public Cell getCell(int col) {
        if (col < 0 || col >= width) {
            throw new IndexOutOfBoundsException("Column " + col + " out of bounds");
        }
        return cells[col];
    }

    public int getWidth() {
        return width;
    }

    public void clear() {
        for (Cell cell : cells) {
            cell.clear();
        }
    }

    public String getText() {
        StringBuilder sb = new StringBuilder();
        for (Cell cell : cells) {
            sb.append(cell.getCharacter());
        }
        return sb.toString();
    }
}
