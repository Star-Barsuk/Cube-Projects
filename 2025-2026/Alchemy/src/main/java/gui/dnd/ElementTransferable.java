package gui.dnd;

import models.Element;
import java.awt.datatransfer.*;

public class ElementTransferable implements Transferable {
    public static final DataFlavor ELEMENT_FLAVOR = new DataFlavor(Element.class, "Element");

    private final Element element;

    public ElementTransferable(Element element) {
        this.element = element;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{ELEMENT_FLAVOR};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return ELEMENT_FLAVOR.equals(flavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
        if (!isDataFlavorSupported(flavor)) {
            throw new UnsupportedFlavorException(flavor);
        }
        return element;
    }
}

