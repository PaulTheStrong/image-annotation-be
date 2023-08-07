package by.pavel.imageannotationbe.model.data;

public record BoundingBox(Integer x1, Integer y1, Integer x2, Integer y2) implements AnnotationData {

    public static final int HEIGHT_IDX = 3;
    public static final int WIDTH_IDX = 2;
    public static final int Y_START_IDX = 1;
    public static final int X_START_IDX = 0;

    public Integer[] as2PointsArray() {
        return new Integer[] {Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2), Math.max(y1, y2)};
    }

    public Integer[] asPointWHArray() {
        return new Integer[] {x1, y1, x2 - x1, y2 - y1};
    }

    public static BoundingBox of2PointArray(Integer[] array) {
        return new BoundingBox(array[X_START_IDX], array[Y_START_IDX], array[WIDTH_IDX], array[HEIGHT_IDX]);
    }

}
