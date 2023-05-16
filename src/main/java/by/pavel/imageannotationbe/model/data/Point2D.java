package by.pavel.imageannotationbe.model.data;

public record Point2D(Integer x, Integer y) {

    public Point2D(Integer[] arr) {
        this(arr[0], arr[1]);
    }
}
