/**
 * See https://github.com/floscher/swinghelper/blob/master/src/java/org/jdesktop/swinghelper/debug/CheckThreadViolationRepaintManager.java
 */
package tbb.x4.ui.util;

import org.jboss.logging.Logger;

import javax.swing.*;
import java.lang.ref.WeakReference;

/**
 * <p>This class is used to detect Event Dispatch Thread rule violations<br>
 * See <a href="http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html">How to Use Threads</a>
 * for more info</p>
 * <p/>
 * <p>This is a modification of original idea of Scott Delap<br>
 * Initial version of ThreadCheckingRepaintManager can be found here<br>
 * <a href="http://www.clientjava.com/blog/2004/08/20/1093059428000.html">Easily Find Swing Threading Mistakes</a>
 * </p>
 *
 * @author Scott Delap
 * @author Alexander Potochkin
 * <p>
 * <a href="https://swinghelper.dev.java.net/">https://swinghelper.dev.java.net/</a>
 */
public class CheckThreadViolationRepaintManager extends RepaintManager {
    private static final Logger LOGGER = Logger.getLogger(CheckThreadViolationRepaintManager.class.getName());

    private static JButton test;
    // it is recommended to pass the complete check
    private boolean completeCheck = true;
    private WeakReference<JComponent> lastComponent;

    public CheckThreadViolationRepaintManager(boolean completeCheck) {
        this.completeCheck = completeCheck;
    }

    public CheckThreadViolationRepaintManager() {
        this(true);
    }

    public static void main(String[] args) throws Exception {
        //set CheckThreadViolationRepaintManager
        RepaintManager.setCurrentManager(new CheckThreadViolationRepaintManager());
        //Valid code
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                test();
            }
        });
        System.out.println("Valid code passed...");
        repaintTest();
        System.out.println("Repaint test - correct code");
        //Invalide code (stack trace expected)
        test();
    }

    static void test() {
        JFrame frame = new JFrame("Am I on EDT?");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JButton("JButton"));
        frame.pack();
        frame.setVisible(true);
        frame.dispose();
    }

    //this test must pass
    static void imageUpdateTest() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JEditorPane editor = new JEditorPane();
        frame.setContentPane(editor);
        editor.setContentType("text/html");
        //it works with no valid image as well
        editor.setText("<html><img src=\"file:\\lala.png\"></html>");
        frame.setSize(300, 200);
        frame.setVisible(true);
    }

    static void repaintTest() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    test = new JButton();
                    test.setSize(100, 100);
                }
            });
        } catch (Exception e) {
            LOGGER.warnf(e, "Exception during invokeAndWait");
        }
        // repaint(Rectangle) should be ok
        test.repaint(test.getBounds());
        test.repaint(0, 0, 100, 100);
        test.repaint();
    }

    public boolean isCompleteCheck() {
        return completeCheck;
    }

    public void setCompleteCheck(boolean completeCheck) {
        this.completeCheck = completeCheck;
    }

    @Override
    public synchronized void addInvalidComponent(JComponent component) {
        checkThreadViolations(component);
        super.addInvalidComponent(component);
    }

    @Override
    public void addDirtyRegion(JComponent component, int x, int y, int w, int h) {
        checkThreadViolations(component);
        super.addDirtyRegion(component, x, y, w, h);
    }

    private void checkThreadViolations(JComponent c) {
        if (!SwingUtilities.isEventDispatchThread() && (completeCheck || c.isShowing())) {
            boolean repaint = false;
            boolean fromSwing = false;
            boolean imageUpdate = false;
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            for (StackTraceElement st : stackTrace) {
                if (repaint && st.getClassName().startsWith("javax.swing.") &&
                        // for details see
                        // https://swinghelper.dev.java.net/issues/show_bug.cgi?id=1
                        !st.getClassName().startsWith("javax.swing.SwingWorker")) {
                    fromSwing = true;
                }
                if (repaint && "imageUpdate".equals(st.getMethodName())) {
                    imageUpdate = true;
                }
                if ("repaint".equals(st.getMethodName())) {
                    repaint = true;
                    fromSwing = false;
                }
                if ("read".equals(st.getMethodName()) && "javax.swing.JEditorPane".equals(st.getClassName())) {
                    // Swing reads html from a background thread
                    return;
                }
            }
            if (imageUpdate) {
                //assuming it is java.awt.image.ImageObserver.imageUpdate(...)
                //image was asynchronously updated, that's ok
                return;
            }
            if (repaint && !fromSwing) {
                //no problems here, since repaint() is thread safe
                return;
            }
            //ignore the last processed component
            if (lastComponent != null && c == lastComponent.get()) {
                return;
            }
            lastComponent = new WeakReference<JComponent>(c);
            violationFound(c, stackTrace);
        }
    }

    protected void violationFound(JComponent c, StackTraceElement[] stackTrace) {
        System.out.println();
        System.out.println("EDT violation detected");
        System.out.println(c);
        for (StackTraceElement st : stackTrace) {
            System.out.println("\tat " + st);
        }
    }
}