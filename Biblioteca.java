/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 *
 * @author Nacho
 */
public class Biblioteca {

    ArrayList<Libro> libroList = new ArrayList<Libro>();

    public int crearLibro(Libro libro) {

        try {
            libroList = leerLibrosXML();

            libroList.add(libro);

            try {
                actualizarXML(libroList);

                Alert a = new Alert(Alert.AlertType.CONFIRMATION);

                a.setHeaderText("¡¡¡EXITO!!!");
                a.setContentText("Se ha añadido el libro al documento XML");
                a.showAndWait();

            } catch (Exception e) {   //si falla escribirlo en el xml borrara el libro del arrayList

                Alert a = new Alert(Alert.AlertType.ERROR);

                a.setHeaderText("¡¡¡ERROR!!!");
                a.setContentText("No se ha podido añadir el libro al documento XML");
                a.showAndWait();
                libroList.remove(libro);
            }

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Biblioteca.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Biblioteca.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Biblioteca.class.getName()).log(Level.SEVERE, null, ex);
        }

        int id = libro.getId();

        return id;
    }

    public static Libro recuperarLibro(int id) {
        Libro l = new Libro();
        l = null;
        try {
            ArrayList<Libro> lista = leerLibrosXML();

            for (Libro lib : lista) {
                if (id == lib.getId()) {
                    l = lib;
                }
            }

        } catch (SAXException ex) {
            Logger.getLogger(Biblioteca.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Biblioteca.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Biblioteca.class.getName()).log(Level.SEVERE, null, ex);
        }

        return l;
    }

    public static ArrayList<Libro> leerLibrosXML() throws ParserConfigurationException, SAXException, IOException {
        //Initialize a list of employees
        ArrayList<Libro> libros = new ArrayList<Libro>();
        Libro libro = null;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("Biblioteca.xml"));
        document.getDocumentElement().normalize();
        NodeList nList = document.getElementsByTagName("libro");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                //Create new Employee Object

                libro = new Libro();
                libro.setId(Integer.parseInt(eElement.getAttribute("id")));
                libro.setTitulo(eElement.getElementsByTagName("titulo").item(0).getTextContent());
                libro.setAutor(eElement.getElementsByTagName("autor").item(0).getTextContent());
                libro.setAño(Integer.parseInt(eElement.getElementsByTagName("anyo").item(0).getTextContent()));
                libro.setEditorial(eElement.getElementsByTagName("editorial").item(0).getTextContent());
                libro.setPaginas(Integer.parseInt(eElement.getElementsByTagName("paginas").item(0).getTextContent()));

                //aqui un if i==0 para mostrarlo la primera vez
                //Add Bicicleta to list
                libros.add(libro);
            }
        }
        return libros;
    }

    public static void actualizarXML(ArrayList<Libro> lista) {
        try {
            

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            DOMImplementation imp = dBuilder.getDOMImplementation();

            Document documento = imp.createDocument(null, "libros", null);
            documento.setXmlVersion("1.0");

            
            for (Libro l : lista) {
                Element libro = documento.createElement("libro");
                libro.setAttribute("id", String.valueOf(l.getId()));

                
                Element titulo = documento.createElement("titulo");
                Text tituloTXT = documento.createTextNode(l.getTitulo());
                titulo.appendChild(tituloTXT);
                libro.appendChild(titulo);

                
                Element autor = documento.createElement("autor");
                Text autorTXT = documento.createTextNode(l.getAutor());
                autor.appendChild(autorTXT);
                libro.appendChild(autor);

                
                Element año = documento.createElement("anyo");
                Text añoTXT = documento.createTextNode(String.valueOf(l.getAño()));
                año.appendChild(añoTXT);
                libro.appendChild(año);

                
                Element editorial = documento.createElement("editorial");
                Text editorialTXT = documento.createTextNode(l.getEditorial());
                editorial.appendChild(editorialTXT);
                libro.appendChild(editorial);

                
                Element paginas = documento.createElement("paginas");
                Text paginasTXT = documento.createTextNode(String.valueOf(l.getPaginas()));
                paginas.appendChild(paginasTXT);
                libro.appendChild(paginas);

                documento.getDocumentElement().appendChild(libro);
            }

            Source src = new DOMSource(documento);
            Result res = new StreamResult(new File("biblioteca.xml"));

            Transformer trans = TransformerFactory.newInstance().newTransformer();
            trans.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            trans.transform(src, res);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static Libro mostrarLibro(Libro l) {

        //preguntar si es un metodo inservible
        /*try {
            ArrayList<Libro> lista = leerLibrosXML();

            for (Libro lib : lista) {
                
            }

        } catch (SAXException ex) {
            Logger.getLogger(Biblioteca.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Biblioteca.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Biblioteca.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        return l;
    }

    public boolean borrarLibro(int id) {

        boolean resultado=false;
        
        try {
            libroList = leerLibrosXML();
            Libro borrar = new Libro();
            borrar = null;

            for (Libro l : libroList) {
                if (id == l.getId()) {
                    borrar = l;
                }
            }

            if (borrar == null) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setHeaderText("¡¡¡ERROR!!!");
                a.setContentText("No hay ningun libro con esa ID");
                a.showAndWait();
            } else {

                libroList.remove(borrar);

                try {
                    actualizarXML(libroList);
                    resultado=true;

                    

                } catch (Exception e) {
                    System.out.println(e);
                }

            }

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Biblioteca.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Biblioteca.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Biblioteca.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultado;
    }
}
