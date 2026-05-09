package gui.dnd;

import models.Element;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;

public class CombinationSlot extends JPanel implements DropTargetListener {
    private Element currentElement;
    private final int slotIndex;
    private final SlotListener listener;
    private JLabel contentLabel;
    private JLabel emptyLabel;

    public interface SlotListener {
        void onElementDropped(int slotIndex, Element element);
        void onElementRemoved(int slotIndex);
    }

    public CombinationSlot(int slotIndex, SlotListener listener) {
        this.slotIndex = slotIndex;
        this.listener = listener;

        setPreferredSize(new Dimension(100, 100));
        setBackground(new Color(230, 230, 240));
        setBorder(BorderFactory.createLineBorder(new Color(180, 180, 200), 2));
        setLayout(new BorderLayout());

        // Создаем метку для отображения элемента
        contentLabel = new JLabel("", SwingConstants.CENTER);
        contentLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        contentLabel.setVisible(false);

        // Метка для пустого слота
        emptyLabel = new JLabel("?", SwingConstants.CENTER);
        emptyLabel.setFont(new Font("Dialog", Font.BOLD, 24));
        emptyLabel.setForeground(new Color(150, 150, 180));

        add(emptyLabel, BorderLayout.CENTER);
        add(contentLabel, BorderLayout.CENTER);

        // Настраиваем DropTarget
        DropTarget dropTarget = new DropTarget(this, this);
        setDropTarget(dropTarget);

        // Добавляем возможность удалить элемент по правому клику
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3 && currentElement != null) {
                    removeElement();
                }
            }
        });
    }

    public void setElement(Element element) {
        this.currentElement = element;
        if (element != null) {
            contentLabel.setText("<html><center>" + element.getName() + "<br><font size='-1'>ур." +
                    element.getLevel() + "</font></center></html>");
            contentLabel.setVisible(true);
            emptyLabel.setVisible(false);
            setBackground(new Color(200, 230, 255));
            setBorder(BorderFactory.createLineBorder(new Color(100, 150, 255), 2));
        } else {
            contentLabel.setVisible(false);
            emptyLabel.setVisible(true);
            setBackground(new Color(230, 230, 240));
            setBorder(BorderFactory.createLineBorder(new Color(180, 180, 200), 2));
        }
        revalidate();
        repaint();
    }

    public Element getElement() {
        return currentElement;
    }

    public void removeElement() {
        if (currentElement != null) {
            Element removed = currentElement;
            setElement(null);
            if (listener != null) {
                listener.onElementRemoved(slotIndex);
            }
        }
    }

    public boolean isEmpty() {
        return currentElement == null;
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        if (dtde.isDataFlavorSupported(ElementTransferable.ELEMENT_FLAVOR)) {
            dtde.acceptDrag(DnDConstants.ACTION_COPY);
            setBackground(new Color(200, 255, 200));
        } else {
            dtde.rejectDrag();
        }
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        // Ничего не делаем
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
        // Ничего не делаем
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
        setBackground(new Color(230, 230, 240));
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        setBackground(new Color(230, 230, 240));

        try {
            if (dtde.isDataFlavorSupported(ElementTransferable.ELEMENT_FLAVOR)) {
                dtde.acceptDrop(DnDConstants.ACTION_COPY);

                Element element = (Element) dtde.getTransferable()
                        .getTransferData(ElementTransferable.ELEMENT_FLAVOR);

                if (currentElement == null) {
                    setElement(element);
                    if (listener != null) {
                        listener.onElementDropped(slotIndex, element);
                    }
                    dtde.dropComplete(true);
                } else {
                    dtde.dropComplete(false);
                }
            } else {
                dtde.rejectDrop();
            }
        } catch (Exception e) {
            e.printStackTrace();
            dtde.dropComplete(false);
        }
    }
}
