/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Creator:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2008, by Barak Naveh and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */
/* ------------------
 * DOTSubgraphExporter.java
 * ------------------
 * (C) Copyright 2006, by Trevor Harmon.
 *
 * Original Author:  Trevor Harmon <trevor@vocaro.com>
 *
 */
package org.candle.decompiler.util;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Map;
import java.util.UUID;

import javax.swing.text.StyleContext.NamedStyle;

import org.candle.decompiler.instruction.graph.vertex.NamedDirectedSubgraph;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.ext.ComponentAttributeProvider;
import org.jgrapht.ext.EdgeNameProvider;
import org.jgrapht.ext.IntegerNameProvider;
import org.jgrapht.ext.VertexNameProvider;
import org.jgrapht.graph.Subgraph;

public class DOTSubgraphExporter<V, E>
{
    //~ Instance fields --------------------------------------------------------
    private VertexNameProvider<V> vertexIDProvider;
    private VertexNameProvider<V> vertexLabelProvider;
    private EdgeNameProvider<E> edgeLabelProvider;
    private ComponentAttributeProvider<V> vertexAttributeProvider;
    private ComponentAttributeProvider<E> edgeAttributeProvider;

    //~ Constructors -----------------------------------------------------------

    /**
     * Constructs a new DOTSubgraphExporter object with an integer name provider for the
     * vertex IDs and null providers for the vertex and edge labels.
     */
    public DOTSubgraphExporter()
    {
        this(new IntegerNameProvider<V>(), null, null);
    }

    /**
     * Constructs a new DOTSubgraphExporter object with the given ID and label
     * providers.
     *
     * @param vertexIDProvider for generating vertex IDs. Must not be null.
     * @param vertexLabelProvider for generating vertex labels. If null, vertex
     * labels will not be written to the file.
     * @param edgeLabelProvider for generating edge labels. If null, edge labels
     * will not be written to the file.
     */
    public DOTSubgraphExporter(
        VertexNameProvider<V> vertexIDProvider,
        VertexNameProvider<V> vertexLabelProvider,
        EdgeNameProvider<E> edgeLabelProvider)
    {
        this(
            vertexIDProvider,
            vertexLabelProvider,
            edgeLabelProvider,
            null,
            null);
    }

    /**
     * Constructs a new DOTSubgraphExporter object with the given ID, label, and
     * attribute providers. Note that if a label provider conflicts with a
     * label-supplying attribute provider, the label provider is given
     * precedence.
     *
     * @param vertexIDProvider for generating vertex IDs. Must not be null.
     * @param vertexLabelProvider for generating vertex labels. If null, vertex
     * labels will not be written to the file (unless an attribute provider is
     * supplied which also supplies labels).
     * @param edgeLabelProvider for generating edge labels. If null, edge labels
     * will not be written to the file.
     * @param vertexAttributeProvider for generating vertex attributes. If null,
     * vertex attributes will not be written to the file.
     * @param edgeAttributeProvider for generating edge attributes. If null,
     * edge attributes will not be written to the file.
     */ 
    public DOTSubgraphExporter(
        VertexNameProvider<V> vertexIDProvider,
        VertexNameProvider<V> vertexLabelProvider,
        EdgeNameProvider<E> edgeLabelProvider,
        ComponentAttributeProvider<V> vertexAttributeProvider,
        ComponentAttributeProvider<E> edgeAttributeProvider)
    {
        this.vertexIDProvider = vertexIDProvider;
        this.vertexLabelProvider = vertexLabelProvider;
        this.edgeLabelProvider = edgeLabelProvider;
        this.vertexAttributeProvider = vertexAttributeProvider;
        this.edgeAttributeProvider = edgeAttributeProvider;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Exports a graph into a plain text file in DOT format.
     *
     * @param writer the writer to which the graph to be exported
     * @param g the graph to be exported
     */
    public void export(Writer writer, Subgraph<V, E, ?> g)
    {
    	String name = UUID.randomUUID().toString();
    	if(g instanceof NamedDirectedSubgraph) {
    		name = ((NamedDirectedSubgraph) g).getName(); 
    	}
    	
        PrintWriter out = new PrintWriter(writer);
        String indent = "  ";
        String connector;

        if (g instanceof DirectedGraph<?, ?>) {
            out.println("subgraph cluster_" + name + " {");
            connector = " -> ";
        } else {
            out.println("subgraph cluster_" + name + " {");
            connector = " -- ";
        }

        for (V v : g.vertexSet()) {
            out.print(indent + getVertexID(v));

            String labelName = null;
            if (vertexLabelProvider != null) {
                labelName = vertexLabelProvider.getVertexName(v);
            }
            Map<String, String> attributes = null;
            if (vertexAttributeProvider != null) {
                attributes = vertexAttributeProvider.getComponentAttributes(v);
            }
            renderAttributes(out, labelName, attributes);

            out.println(";");
        }

        out.println("label=\""+name+"\";");
        out.println("}");

        out.flush();
    }

    private void renderAttributes(
        PrintWriter out,
        String labelName,
        Map<String, String> attributes)
    {
        if ((labelName == null) && (attributes == null)) {
            return;
        }
        out.print(" [ ");
        if ((labelName == null) && (attributes != null)) {
            labelName = attributes.get("label");
        }
        if (labelName != null) {
            out.print("label=\"" + labelName + "\" ");
        }
        if (attributes != null) {
            for (Map.Entry<String, String> entry : attributes.entrySet()) {
                String name = entry.getKey();
                if (name.equals("label")) {
                    // already handled by special case above
                    continue;
                }
                out.print(name + "=\"" + entry.getValue() + "\" ");
            }
        }
        out.print("]");
    }

    /**
     * Return a valid vertex ID (with respect to the .dot language definition as
     * described in http://www.graphviz.org/doc/info/lang.html Quoted from above
     * mentioned source: An ID is valid if it meets one of the following
     * criteria:
     *
     * <ul>
     * <li>any string of alphabetic characters, underscores or digits, not
     * beginning with a digit;
     * <li>a number [-]?(.[0-9]+ | [0-9]+(.[0-9]*)? );
     * <li>any double-quoted string ("...") possibly containing escaped quotes
     * (\");
     * <li>an HTML string (<...>).
     * </ul>
     *
     * @throws RuntimeException if the given <code>vertexIDProvider</code>
     * didn't generate a valid vertex ID.
     */
    private String getVertexID(V v)
    {
        // TODO jvs 28-Jun-2008:  possible optimizations here are
        // (a) only validate once per vertex
        // (b) compile regex patterns

        // use the associated id provider for an ID of the given vertex
        String idCandidate = vertexIDProvider.getVertexName(v);

        // now test that this is a valid ID
        boolean isAlphaDig = idCandidate.matches("[a-zA-Z]+([\\w_]*)?");
        boolean isDoubleQuoted = idCandidate.matches("\".*\"");
        boolean isDotNumber =
            idCandidate.matches("[-]?([.][0-9]+|[0-9]+([.][0-9]*)?)");
        boolean isHTML = idCandidate.matches("<.*>");

        if (isAlphaDig || isDotNumber || isDoubleQuoted || isHTML) {
            return idCandidate;
        }

        throw new RuntimeException(
            "Generated id '" + idCandidate + "'for vertex '" + v
            + "' is not valid with respect to the .dot language");
    }
}

// End DOTSubgraphExporter.java
