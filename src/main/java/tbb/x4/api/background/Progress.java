package tbb.x4.api.background;

import java.util.Objects;

public class Progress {
    private final ProgressMode mode;
    private final double progress;

    public Progress(ProgressMode mode, double progress) {
        if (mode == null) {
            throw new IllegalArgumentException("Progress mode cannot be null");
        }
        if (progress < 0 || progress > 1) {
            throw new IllegalArgumentException("Progress must be between 0 and 1");
        }
        this.mode = mode;
        this.progress = progress;
    }

    public static Progress undetermined() {
        return new Progress(ProgressMode.UNDETERMINED, 0);
    }

    public static Progress determined(double progress) {
        return new Progress(ProgressMode.DETERMINED, progress);
    }

    public ProgressMode mode() {
        return mode;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Progress progress1 = (Progress) o;
        return Double.compare(progress, progress1.progress) == 0 && mode == progress1.mode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mode, progress);
    }

    public double progress() {
        if (mode == ProgressMode.UNDETERMINED) {
            throw new IllegalStateException("Progress is undetermined, cannot retrieve progress value");
        }
        return progress;
    }

    public enum ProgressMode {
        DETERMINED,
        UNDETERMINED
    }
}
