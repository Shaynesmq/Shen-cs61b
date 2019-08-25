import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;
    private int width;
    private int height;

    public SeamCarver(Picture picture) {
        this.picture = picture;
        this.width = picture.width();
        this.height = picture.height();
    }

    public Picture picture() {
        return new Picture(this.picture);
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public double energy(int x, int y) {
        if (x < 0 || x >= width) {
            throw new IndexOutOfBoundsException();
        }
        if (y < 0 || y >= height) {
            throw new IndexOutOfBoundsException();
        }

        int lx = x == 0 ? width - 1 : x - 1;
        int rx = x == width - 1 ? 0 : x + 1;
        int uy = y == 0 ? height - 1 : y - 1;
        int ly = y == height - 1 ? 0 : y + 1;

        int lxr = picture.get(lx, y).getRed();
        int lxg = picture.get(lx, y).getGreen();
        int lxb = picture.get(lx, y).getBlue();
        int rxr = picture.get(rx, y).getRed();
        int rxg = picture.get(rx, y).getGreen();
        int rxb = picture.get(rx, y).getBlue();

        int uyr = picture.get(x, uy).getRed();
        int uyg = picture.get(x, uy).getGreen();
        int uyb = picture.get(x, uy).getBlue();
        int lyr = picture.get(x, ly).getRed();
        int lyg = picture.get(x, ly).getGreen();
        int lyb = picture.get(x, ly).getBlue();

        double xr = Math.pow(lxr - rxr, 2);
        double xg = Math.pow(lxg - rxg, 2);
        double xb = Math.pow(lxb - rxb, 2);
        double yr = Math.pow(uyr - lyr, 2);
        double yg = Math.pow(uyg - lyg, 2);
        double yb = Math.pow(uyb - lyb, 2);

        return xr + xg + xb + yr + yg + yb;
    }

    public int[] findHorizontalSeam() {
        int[] result = new int[width];

        double[][] costGrid = new double[height][width];

        for (int y = 0; y < height; y++) {
            costGrid[y][0] = energy(0, y);
        }

        for (int x = 1; x < width; x++) {
            for (int y = 0; y < height; y++) {
                costGrid[y][x] = costGrid[findMinY(x - 1, y, costGrid)][x - 1] + energy(x, y);
            }
        }

        double eMin = Double.POSITIVE_INFINITY;
        int minEnd = 0;
        for (int j = 0; j < height; j++) {
            if (costGrid[j][width - 1] < eMin) {
                eMin = costGrid[j][width - 1];
                minEnd = j;
            }
        }

        int x = width - 1;
        while (x > 0) {
            result[x] = minEnd;
            minEnd = findMinY(x - 1, minEnd, costGrid);
            x--;
        }
        result[x] = minEnd;

        return result;
    }

    private int findMinY(int x, int y, double[][] cg) {
        if (height == 1) {
            return y;
        }

        if (y == 0) {
            return cg[y][x] > cg[y + 1][x] ? y + 1 : y;
        } else if (y == height - 1) {
            return cg[y][x] > cg[y - 1][x] ? y - 1 : y;
        }

        if (cg[y - 1][x] > cg[y][x]) {
            if (cg[y + 1][x] > cg[y][x]) {
                return y;
            } else {
                return y + 1;
            }
        } else {
            if (cg[y + 1][x] > cg[y - 1][x]) {
                return y - 1;
            } else {
                return y + 1;
            }
        }
    }

    public int[] findVerticalSeam() {
        int[] result = new int[height];

        double[][] costGrid = new double[height][width];

        for (int x = 0; x < width; x++) {
            costGrid[0][x] = energy(x, 0);
        }

        for (int y = 1; y < height; y++) {
            for (int x = 0; x < width; x++) {
                costGrid[y][x] = costGrid[y - 1][findMinX(x, y - 1, costGrid)] + energy(x, y);
            }
        }

        double eMin = Double.POSITIVE_INFINITY;
        int minEnd = 0;
        for (int i = 0; i < width; i++) {
            if (costGrid[height - 1][i] < eMin) {
                eMin = costGrid[height - 1][i];
                minEnd = i;
            }
        }

        int y = height - 1;
        while (y > 0) {
            result[y] = minEnd;
            minEnd = findMinX(minEnd, y - 1, costGrid);
            y--;
        }
        result[y] = minEnd;

        return result;
    }

    private int findMinX(int x, int y, double[][] cg) {
        if (width == 1) {
            return x;
        }

        if (x == 0) {
            return cg[y][x] > cg[y][x + 1] ? x + 1 : x;
        } else if (x == width - 1) {
            return cg[y][x] > cg[y][x - 1] ? x - 1 : x;
        }

        if (cg[y][x - 1] > cg[y][x]) {
            if (cg[y][x + 1] > cg[y][x]) {
                return x;
            } else {
                return x + 1;
            }
        } else {
            if (cg[y][x + 1] > cg[y][x - 1]) {
                return x - 1;
            } else {
                return x + 1;
            }
        }
    }

    public void removeHorizontalSeam(int[] seam) {
        if (seam.length != width) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < width - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new IllegalArgumentException();
            }
        }

        picture = new Picture(SeamRemover.removeHorizontalSeam(picture, seam));
        height--;
    }

    public void removeVerticalSeam(int[] seam) {
        if (seam.length != height) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < height - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new IllegalArgumentException();
            }
        }

        picture = new Picture(SeamRemover.removeVerticalSeam(picture, seam));
        width--;
    }

}
