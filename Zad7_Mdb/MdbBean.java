import pl.jrj.mdb.IMdbSession;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;


@MessageDriven(mappedName = "jms/MyQueue", activationConfig = {
        @ActivationConfigProperty(
                propertyName = "destinationType",
                propertyValue = "javax.jms.Queue"
        ),
        @ActivationConfigProperty(
                propertyName = "acknowledgeMode",
                propertyValue = "Auto-acknowledge"
        )
})
public class MdbBean implements MessageListener {

    boolean counting = false;
    int errors = 0;
    int counter = 0;

    String sessionId = null;

    private ConnectionFactory connectionFactory;
    private Destination destination;

    /**
     * Konstruktor
     */
    public MdbBean() {
        InitialContext context = null;
        try {
            context = new InitialContext();
            IMdbSession iMdbSession = (IMdbSession) context.
                    lookup("java:global/mdb-project/MdbManager!pl.jrj.mdb.IMdbSession");
            connectionFactory = (ConnectionFactory) context.
                    lookup("jms/MyConnectionFactory");
            destination = (Destination) context.lookup("jms/MyTopic");
            sessionId = iMdbSession.sessionId("136933");
        } catch (NamingException e) {
            e.printStackTrace();
        }

    }

    /**
     * Obsuga wiadomosci od zasobu
     * @param message
     */
    @Override
    public void onMessage(Message message) {

        try {
            TextMessage tm = (TextMessage) message;
            String mess = tm.getText();

            if ("start".equals(mess)) {
                start();
            } else if ("stop".equals(mess)) {
                stop();
            } else if ("counter".equals(mess)) {
                result(counter);
            } else if ("error".equals(mess)) {
                result(errors);
            } else if ("increment".equals(mess)) {
                increment(1);
            } else if (mess.startsWith("increment/")) {
                String value = mess.split("/")[1];
                try {
                    int incr = Integer.parseInt(value);
                    increment(incr);
                } catch (NumberFormatException e) {
                    errors++;
                }

            } else {
                errors++;
            }

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }


    /**
     * Rozpoczecie zliczania
     */
    private void start() {

        if (!counting) {
            counting = true;
        } else errors++;
    }

    /**
     * Zatrzymanie zliczania
     */
    private void stop() {

        if (counting) {
            counting = false;
        } else errors++;
    }

    /**
     * Funkcja zwieksza wartosc licznika
     * @param value ilosc o jaka ma byc zwiekszony
     *              licznik
     */
    private void increment(int value) {

        if (counting) {
            counter += value;
        } else errors++;
    }

    /**
     * Funkcja zwaracajaca wynik
     * @param result - licznik
     */
    private void result(int result) {

        try {
            Connection con = connectionFactory.createConnection();
            Session session = con.createSession();
            MessageProducer messageProducer = session.createProducer(destination);
            con.start();
            TextMessage textMessage = session.createTextMessage();
            textMessage.setText(sessionId + "/" + result);
            messageProducer.send(textMessage);
            con.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
