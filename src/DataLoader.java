import java.io.*;
        import java.util.*;

        import javax.swing.*;
        import javax.xml.parsers.*;
        import javax.xml.xpath.*;
        import org.w3c.dom.*;
        import org.xml.sax.SAXException;


public class DataLoader {
    private DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder builder;
    private Document doc = null;
    private XPath xpath;
    private XPathFactory xpathFactory = XPathFactory.newInstance();

    private String file;

    private Map<String, PersonalItem> mapPersons = new HashMap<String, PersonalItem>();
    private String[] listID = new String[0];

    public JTextArea strResult = null;

    public Map<String, PersonalItem> getPersonMap(){
        return mapPersons;
    }

    public String[] getIDList(){
        return listID;
    }

    public DataLoader() {
        // включаем поддержку пространства имен XML
        builderFactory.setNamespaceAware(true);
        xpath = xpathFactory.newXPath();
    }

    public int open(String sFile){
        this.file = sFile;

        XPathExpression xPathExpression;
        NodeList xPersons, xParams;

        mapPersons = new HashMap<String, PersonalItem>();
        try {
            builder = builderFactory.newDocumentBuilder();
            doc = builder.parse(sFile);

            try {
                xPathExpression = xpath.compile("//Person");
                xPersons = (NodeList) xPathExpression.evaluate(doc, XPathConstants.NODESET);
                listID = new String[xPersons.getLength()];
                for (int i = 0; i < xPersons.getLength(); i++){
                    listID[i] = xPersons.item(i).getAttributes().getNamedItem("ID").getNodeValue();
                    xParams = xPersons.item(i).getChildNodes();
                    WriteMsg("ID: " + listID[i] + ": ");
                    PersonalItem pit = new PersonalItem();
                    pit.setID(listID[i]);
                    for (int j = 0; j < xParams.getLength(); j++){
                        switch(xParams.item(j).getNodeName()) {
                            case "Surname":
                                pit.setSurname(xParams.item(j).getTextContent());
                                WriteMsg(j+": "+xParams.item(j).getNodeName() + ": " + xParams.item(j).getTextContent());
                                break;
                            case "Name":
                                pit.setName(xParams.item(j).getTextContent());
                                WriteMsg(j+": "+xParams.item(j).getNodeName() + ": " + xParams.item(j).getTextContent());
                                break;
                            case "Patronymic":
                                pit.setPatronymic(xParams.item(j).getTextContent());
                                WriteMsg(j+": "+xParams.item(j).getNodeName() + ": " + xParams.item(j).getTextContent());
                                break;
                            case "Birthday":
                                pit.setBirthday(xParams.item(j).getTextContent());
                                WriteMsg(j+": "+xParams.item(j).getNodeName() + ": " + xParams.item(j).getTextContent());
                                break;
                        }
                    }
                    mapPersons.put(pit.getID(), pit);
                }
                //listID.add(nodes.item(i).getAttributes().getNamedItem("ID").getNodeValue());
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    private void WriteMsg(String sMes){
        if (strResult == null)
            return;
        strResult.setText(strResult.getText() + "\n" + sMes);
    }
}

