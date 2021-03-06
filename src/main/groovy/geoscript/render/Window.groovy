package geoscript.render

import groovy.swing.SwingBuilder
import java.awt.BorderLayout
import javax.swing.JFrame

/**
 * A simple GUI for viewing a {@link geoscript.render.Map Map}.
 * <p><blockquote><pre>
 * import geoscript.render.*
 * import geoscript.layer.*
 * import geoscript.style.*
 *
 * Map map = new Map(layers:[new Shapefile("states.shp")])
 * new Window(map)
 * </pre></blockquote></p>
 * @author Jared Erickson
 */
class Window {

    /**
     * Open a simple GUI for viewing a Map
     * @param map The Map
     */
    Window(Map map) {
        def swing = new SwingBuilder()
        def frame = swing.frame(title:'Window', size:[map.width, map.height], show:false) {
            borderLayout()
            label(icon:imageIcon(map.renderToImage()), size:[map.width, map.height],constraints: BorderLayout.CENTER)
        }
        // If we are opening Windows from the GroovyConsole, we can't use EXIT_ON_CLOSE because the GroovyConsole
        // itself will exit
        if (java.awt.Frame.frames.find{it.title.contains("GroovyConsole")}) {
            frame.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
        } else {
            // The Groovy Shell has a special SecurityManager that doesn't allow EXIT_ON_CLOSE
            try { frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE } catch (SecurityException ex) {frame.defaultCloseOperation = JFrame.HIDE_ON_CLOSE}
        }
        swing.edt {
            frame.visible = true
        }
    }
}



