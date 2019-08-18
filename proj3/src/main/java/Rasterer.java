import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    private int depth;

    private String[][] grid;
    private int ulx;
    private int uly;
    private int lrx;
    private int lry;

    private double raster_ul_lon;
    private double raster_ul_lat;
    private double raster_lr_lon;
    private double raster_lr_lat;

    private boolean query_success;

    public Rasterer() {
        depth = 0;
        grid = null;
        ulx = 0;
        uly = 0;
        lrx = 0;
        lry = 0;
        raster_ul_lon = 0;
        raster_ul_lat = 0;
        raster_lr_lon = 0;
        raster_lr_lat = 0;
        query_success = true;
    }

    private double queryLonDPP(double lrlon, double ullon, double width) {
        return (lrlon - ullon) / width;
    }

    private void getDepth(double queryLonDPP) {
        int depth = 0;
        double LonDPP = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / 256;
        while (LonDPP > queryLonDPP && depth < 7) {
            LonDPP /= 2;
            depth += 1;
        }
        this.depth = depth;
    }

    private double lonStep(int depth) {
        return (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / Math.pow(2, depth);
    }

    private double latStep(int depth) {
        return (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / Math.pow(2, depth);
    }

    private int getULX(double lon) {
        if (lon < MapServer.ROOT_ULLON) return 0;
        double step = lonStep(depth);
        return (int) ((lon - MapServer.ROOT_ULLON) / step);
    }

    private int getULY(double lat) {
        if (lat > MapServer.ROOT_ULLAT) return 0;
        double step = latStep(depth);
        return (int) ((MapServer.ROOT_ULLAT - lat) / step);
    }

    private int getLRX(double lon) {
        if (lon > MapServer.ROOT_LRLON) return (int) Math.pow(2, depth) - 1;
        double step = lonStep(depth);
        if ((lon - MapServer.ROOT_ULLON) % step == 0) {
            return (int) ((lon - MapServer.ROOT_ULLON) / step - 1);
        } else {
            return (int) ((lon - MapServer.ROOT_ULLON) / step);
        }
    }

    private int getLRY(double lat) {
        if (lat < MapServer.ROOT_LRLAT) return (int) Math.pow(2, depth) - 1;
        double step = latStep(depth);
        if ((MapServer.ROOT_ULLAT - lat) % step == 0) {
            return (int) ((MapServer.ROOT_ULLAT - lat) / step - 1);
        } else {
            return (int) ((MapServer.ROOT_ULLAT - lat) / step);
        }
    }

    private void bb(Map<String, Double> params) {
        double ullon = params.get("ullon");
        double ullat = params.get("ullat");
        double lrlon = params.get("lrlon");
        double lrlat = params.get("lrlat");
        double w = params.get("w");
        double h = params.get("h");

        checkValid(ullon, ullat, lrlon, lrlat, w, h);
        if (!query_success) {
            return;
        }

        double QLDPP = queryLonDPP(lrlon, ullon, w);
        getDepth(QLDPP);
        ulx = getULX(ullon);
        uly = getULY(ullat);
        lrx = getLRX(lrlon);
        lry = getLRY(lrlat);

        raster_ul_lon = MapServer.ROOT_ULLON + ulx * lonStep(depth);
        raster_ul_lat = MapServer.ROOT_ULLAT - uly * latStep(depth);
        raster_lr_lon = MapServer.ROOT_ULLON + (lrx + 1) * lonStep(depth);
        raster_lr_lat = MapServer.ROOT_ULLAT - (lry + 1) * latStep(depth);

        setGrid();
    }

    private void setGrid() {
        int szx = lrx - ulx + 1;
        int szy = lry - uly + 1;
        grid = new String[szy][szx];

        int i = 0;
        int j = 0;
        for (int y = uly; y <= lry; y += 1, j = 0) {
            for (int x = ulx; x <= lrx; x += 1) {
                grid[i][j] = getFileName(x, y);
                j += 1;
            }
            i += 1;
        }
    }

    private String getFileName(int x, int y) {
        return "d" + depth + "_x" + x + "_y" + y + ".png";
    }

    private void checkValid(double ullon, double ullat, double lrlon, double lrlat, double w, double h) {
        if (ullon < MapServer.ROOT_ULLON && lrlon < MapServer.ROOT_ULLON) {
            query_success = false;
        }
        if (ullon > MapServer.ROOT_LRLON && lrlon > MapServer.ROOT_LRLON) {
            query_success = false;
        }
        if (ullat < MapServer.ROOT_LRLAT && lrlat < MapServer.ROOT_LRLAT) {
            query_success = false;
        }
        if (ullat > MapServer.ROOT_ULLAT && lrlat > MapServer.ROOT_ULLAT) {
            query_success = false;
        }

        if (ullon >= lrlon || ullat <= lrlat) {
            query_success = false;
        }

        if (w <= 0 || h <= 0) {
            query_success = false;
        }
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        Map<String, Object> results = new HashMap<>();
        bb(params);

        results.put("render_grid", grid);
        results.put("raster_ul_lon", raster_ul_lon);
        results.put("raster_ul_lat", raster_ul_lat);
        results.put("raster_lr_lon", raster_lr_lon);
        results.put("raster_lr_lat", raster_lr_lat);
        results.put("depth", depth);
        results.put("query_success", query_success);
        return results;
    }


    public static void main(String[] args) {
        Rasterer r = new Rasterer();
        Map<String, Double> params = new HashMap<>();
        params.put("lrlon", -122.2104604264636);
        params.put("ullon", -122.30410170759153);
        params.put("w", 1085.0);
        params.put("h", 566.0);
        params.put("ullat", 37.870213571328854);
        params.put("lrlat", 37.8318576119893);
        System.out.println(params);
        String[][] s = (String[][]) r.getMapRaster(params).get("render_grid");
        System.out.println(s[2][3]);
    }
}
