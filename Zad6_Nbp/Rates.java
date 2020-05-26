import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.HashMap;

public class Rates {

    HashMap<String, Double> ratesMap;

    public Rates(){
        ratesMap = new HashMap<>();
    }

    /**
     * Funkcja pobiera dane o srednim kursie z NPB a nastepnie oblicza sredni kurs
     * dla danych wejsciowych
     * @param codeFrom waluta wejsciowa
     * @param codeTo waluta wyjsciowa
     * @return sredni kurs waluty wejsciowej wyrazony w walucie wyjsciowej
     */
    public Double getRates(String codeFrom, String codeTo){
            DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
            double result = 1d;
            ratesMap.put("PLN", 1d);
            try
            {
                DocumentBuilder db = factory.newDocumentBuilder();
                Document doc = db.parse(new URL("https://www.nbp.pl/kursy/xml/a087z200506.xml").openStream());
                Element documentElement=doc.getDocumentElement();
                NodeList sList=documentElement.getElementsByTagName("pozycja");
                if (sList != null && sList.getLength() > 0)
                {
                    for (int i = 0; i < sList.getLength(); i++)
                    {
                        Node node = sList.item(i);
                        if(node.getNodeType()==Node.ELEMENT_NODE)
                        {
                            Element e = (Element) node;

                            Node code = e.getElementsByTagName("kod_waluty").item(0);
                            Node value = e.getElementsByTagName("przelicznik").item(0);
                            Node avg = e.getElementsByTagName("kurs_sredni").item(0);

                            Double val = Double.parseDouble(value.getTextContent());
                            Double av = Double.parseDouble(avg.getTextContent().replace(',','.'));
                            Double rate = val * av;

                            ratesMap.put(code.getFirstChild().getTextContent(), rate);
                        }
                    }
                }
            } catch(Exception e){
                e.printStackTrace();
            }
            Double value1 = ratesMap.get(codeFrom);
            Double value2 = ratesMap.get(codeTo);

            result = result / value1;
            result = result * value2;

            return result;
        }

}
