
package eu.diversify.disco.cloudml.util.actions;


public abstract class AbstractAction<T> implements Action<T> {

    private final StandardLibrary library;

    public AbstractAction(StandardLibrary library) {
        this.library = library;
    }

    public StandardLibrary getLibrary() {
        return library;
    }

}
