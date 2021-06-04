package com.cpd.debate_2.presenter;

import com.cpd.debate_2.debaters_order.TransRecvConnection;
import com.cpd.debate_2.topics_pub_sub.Publisher;
import com.cpd.debate_2.topics_pub_sub.Subscriber;
import com.cpd.debate_2.view.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Presenter {
    private final Timer messageUpdateTimer, buttonTimer;
    private final Subscriber subscriber;
    private final Publisher publisher;
    private final TransRecvConnection transRecvConnection;
    private final View viewElectricGuitars, viewFootball;

    public Presenter(Subscriber subscriber, Publisher publisher,
                     String frameNameElectricGuitars, String frameNameFootball) throws IOException {
        this.subscriber = subscriber;
        this.publisher = publisher;

        transRecvConnection = new TransRecvConnection();
        transRecvConnection.start();

        viewElectricGuitars = new View(frameNameElectricGuitars);
        viewFootball = new View(frameNameFootball);

        viewElectricGuitars.getSendButton().addActionListener(new SendElectricGuitarsMessageListener());
        viewFootball.getSendButton().addActionListener(new SendFootballMessageListener());


        messageUpdateTimer = new Timer(100, new UpdateMessagesListener());
        messageUpdateTimer.start();

        buttonTimer = new Timer(100, new UpdateButtonListener());
        buttonTimer.start();
    }

    public class UpdateMessagesListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            viewElectricGuitars.getTextArea().setText(subscriber.getElectricGuitarsMessages());
            viewFootball.getTextArea().setText(subscriber.getFootballMessages());
        }
    }

    public class UpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (transRecvConnection.getTokenState().contains("debate")) {
                viewElectricGuitars.getSendButton().setEnabled(true);
                viewFootball.getSendButton().setEnabled(true);
            } else {
                viewElectricGuitars.getSendButton().setEnabled(false);
                viewFootball.getSendButton().setEnabled(false);
            }
        }
    }

    public class SendElectricGuitarsMessageListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String messageToSend = viewElectricGuitars.getTextField().getText();

            if (messageToSend.length() != 0)
                publisher.publishMessageOnTopic(messageToSend, "electric_guitars");

            viewElectricGuitars.getTextField().setText("");
        }
    }

    public class SendFootballMessageListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String messageToSend = viewFootball.getTextField().getText();

            if (messageToSend.length() != 0)
                publisher.publishMessageOnTopic(messageToSend, "football");

            viewFootball.getTextField().setText("");
        }
    }

    public void showFrames() {
        viewElectricGuitars.getFrame().setVisible(true);
        viewFootball.getFrame().setVisible(true);
    }
}
