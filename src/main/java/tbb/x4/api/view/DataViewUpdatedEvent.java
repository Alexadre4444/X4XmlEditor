package tbb.x4.api.view;

/**
 * Event indicating that a data view has been updated.
 */
public class DataViewUpdatedEvent {
    private final IDataView dataView;

    public DataViewUpdatedEvent(IDataView dataView) {
        this.dataView = dataView;
    }

    /**
     * Returns the data view that has been updated.
     *
     * @return the updated data view
     */
    public IDataView dataView() {
        return dataView;
    }
}
