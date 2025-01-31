import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FocusUtil {
    public static void disableAutoFocus(JComponent... components) {
        for (JComponent component : components) {
            component.setFocusable(false);

            component.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    component.setFocusable(true);
                    component.requestFocus();
                }
            });
        }
    }
}
