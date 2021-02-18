package minesweeper;

import java.util.Random;
import java.util.Scanner;

public class Field {
    String[][] field = new String[9][9];
    int mines;
    String[][] view = new String[9][9];
    int[][] countMine = new int[11][11];


    {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                field[i][j] = ".";
            }
        }

        for (int i = 0; i < view.length; i++) {
            for (int j = 0; j < view[0].length; j++) {
                view[i][j] = ".";
            }
        }
    }

    public Field(int mines) {
        this.mines = mines;
    }

    public String[] putMines() {
        Random random = new Random();
        int x;
        int y;
        boolean again;

        Scanner scan = new Scanner(System.in);
        String[] firstAim;
        int b;
        int a;
        do {
            System.out.print("Set/delete mines marks (x and y coordinates): ");
            firstAim = scan.nextLine().split(" ");
            b = Integer.parseInt(firstAim[0]) - 1;
            a = Integer.parseInt(firstAim[1]) - 1;
            if ("mine".equals(firstAim[2])) {
                if (".".equals(view[a][b])) {
                    view[a][b] = "*";
                } else {
                    view[a][b] = ".";
                }
                getField();
            }
        } while (!"free".equals(firstAim[2]));

        for (int m = 0; m < mines; m++) {
            do {
                x = random.nextInt(this.field.length);
                y = random.nextInt(this.field[0].length);
                if (x == a && y == b || x == b && y == a) {
                    again = true;
                } else if (".".equals(this.field[x][y])) {
                    this.field[x][y] = "X";
                    again = false;
                } else {
                    again = true;
                }
            } while (again);
        }

        return firstAim;
    }

    public void getField() {
        System.out.println("\n |123456789|");
        System.out.println("-|---------|");
        for (int i = 0; i < view.length; i++) {
            System.out.print(i + 1 + "|");
            for (int j = 0; j < view[0].length; j++) {
                System.out.print(view[i][j]);
            }
            System.out.println("|");
        }
        System.out.println("-|---------|");
    }

    public void howManyMines() {
        for (int i = 0; i < this.field.length; i++) {
            for (int j = 0; j < this.field[0].length; j++) {
                if ("X".equals(this.field[i][j])) {
                    countMine[i + 1][j + 1] = 1;
                }
            }
        }

        for (int i = 1; i < countMine.length - 1; i++) {
            for (int j = 1; j < countMine[0].length - 1; j++) {
                if (countMine[i][j] != 1) {
                    this.field[i - 1][j - 1] = String.valueOf(countMine[i - 1][j - 1] + countMine[i - 1][j] + countMine[i - 1][j + 1]
                            + countMine[i][j - 1] + countMine[i][j + 1]
                            + countMine[i + 1][j - 1] + countMine[i + 1][j] + countMine[i + 1][j + 1]);
                }
                if ("0".equals(this.field[i - 1][j - 1])) {
                    this.field[i - 1][j - 1] = "/";
                }
            }
        }
    }

    public void play(String[] firstAim) {
        String[][] playground = new String[9][9];
        for (int i = 0; i < this.field.length; i++) {
            for (int j = 0; j < this.field[0].length; j++) {
                playground[i][j] = this.field[i][j];
            }
        }

        Scanner scanner = new Scanner(System.in);
        String[] aim;
        int x;
        int y;
        int counterX = this.mines;
        int counterO = 0;
        boolean firstShot = true;
        boolean notFree;
        while(true) {
            if (firstShot) {
                y = Integer.parseInt(firstAim[0]) - 1;
                x = Integer.parseInt(firstAim[1]) - 1;
                notFree = false;
                firstShot = false;
            } else {
                System.out.print("Set/delete mines marks (x and y coordinates): ");
                aim = scanner.nextLine().split(" ");
                y = Integer.parseInt(aim[0]) - 1;
                x = Integer.parseInt(aim[1]) - 1;
                notFree = "mine".equals(aim[2]);
            }

            if ("X".equals(this.field[x][y]) && notFree && ".".equals(view[x][y])) {
                this.field[x][y] = "*";
                view[x][y] = "*";
                counterO++;
                counterX--;
                getField();
            } else if ("X".equals(this.field[x][y]) && !notFree && ".".equals(view[x][y])) {
                for (int p = 0; p < this.field.length; p++) {
                    for (int r = 0; r < this.field[0].length; r++) {
                        if ("X".equals(playground[p][r])) {
                            view[p][r] = "X";
                        }
                    }
                }
                getField();
                System.out.println("You stepped on a mine and failed!");
                System.exit(0);
            } else if ("/".equals(this.field[x][y]) && notFree && ".".equals(view[x][y])) {
                this.field[x][y] = "*";
                view[x][y] = "*";
                counterO++;
                getField();
            } else if ("/".equals(this.field[x][y]) && !notFree && ".".equals(view[x][y])) {
                view[x][y] = "/";
                expand(playground, x, y);
                getField();
            } else if ("*".equals(this.field[x][y]) && notFree) {
                this.field[x][y] = playground[x][y];
                view[x][y] = ".";
                counterO--;
                if ("X".equals(playground[x][y])) {
                    counterX++;
                }
                getField();
            } else if ("*".equals(view[x][y]) && notFree) {
                view[x][y] = ".";
                getField();
            } else if (Character.isDigit(this.field[x][y].charAt(0)) && notFree && ".".equals(view[x][y])) {
                this.field[x][y] = "*";
                view[x][y] = "*";
                counterO++;
                getField();
            } else if (Character.isDigit(this.field[x][y].charAt(0)) && !notFree && ".".equals(view[x][y])) {
                view[x][y] = this.field[x][y];
                getField();
            } else if (Character.isDigit(this.field[x][y].charAt(0)) && Character.isDigit(view[x][y].charAt(0))) {
                System.out.println("There is a number here!");
            }else {
                continue;
            }

            if (counterO == this.mines && counterX == 0) {
                getField();
                System.out.println("Congratulations! You found all mines!");
                break;
            }
        }
    }

    private void expand(String[][] playground, int x, int y) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                try {
                    if (i == 0 && j == 0) {
                        continue;
                    } else if (!"X".equals(playground[x + i][y + j]) && (".".equals(view[x + i][y + j]) || "*".equals(view[x + i][y + j]))) {
                        view[x + i][y + j] = playground[x + i][y + j];
                        if (!Character.isDigit(playground[x + i][y + j].charAt(0))) {
                            expand(playground, x + i, y + j);
                        }
                    }
                } catch (Exception e){}
            }
        }
    }
}
