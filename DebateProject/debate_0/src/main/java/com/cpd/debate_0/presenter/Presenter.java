package com.cpd.debate_0.presenter;

import com.cpd.debate_0.debaters_order.TransRecvConnection;
import com.cpd.debate_0.topics_pub_sub.Publisher;
import com.cpd.debate_0.topics_pub_sub.Subscriber;
import com.cpd.debate_0.view.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Presenter {
    private final Timer messageUpdateTimer, buttonTimer;
    private final Subscriber subscriber;
    private final Publisher publisher;
    private final TransRecvConnection transRecvConnection;
    private final View viewDataScience, viewElectricGuitars, viewFootball;

    public Presenter(Subscriber subscriber, Publisher publisher, String frameNameDataScience,
                     String frameNameElectricGuitars, String frameNameFootball) throws IOException {
        this.subscriber = subscriber;
        this.publisher = publisher;

        viewDataScience = new View(frameNameDataScience);
        viewElectricGuitars = new View(frameNameElectricGuitars);
        viewFootball = new View(frameNameFootball);

        viewDataScience.getSendButton().addActionListener(new SendDataScienceMessageListener());
        viewElectricGuitars.getSendButton().addActionListener(new SendElectricGuitarsMessageListener());
        viewFootball.getSendButton().addActionListener(new SendFootballMessageListener());

        transRecvConnection = new TransRecvConnection();
        transRecvConnection.start();

        messageUpdateTimer = new Timer(100, new UpdateMessagesListener());
        messageUpdateTimer.start();

        buttonTimer = new Timer(100, new UpdateButtonListener());
        buttonTimer.start();
    }

    public class UpdateMessagesListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            viewDataScience.getTextArea().setText(subscriber.getDataScienceMessages());
            viewElectricGuitars.getTextArea().setText(subscriber.getElectricGuitarsMessages());
            viewFootball.getTextArea().setText(subscriber.getFootballMessages());
        }
    }

    public class UpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (transRecvConnection.getTokenState().contains("debate")) {
                viewDataScience.getSendButton().setEnabled(true);
                viewElectricGuitars.getSendButton().setEnabled(true);
                viewFootball.getSendButton().setEnabled(true);
            } else {
                viewDataScience.getSendButton().setEnabled(false);
                viewElectricGuitars.getSendButton().setEnabled(false);
                viewFootball.getSendButton().setEnabled(false);
            }
        }
    }

    public class SendDataScienceMessageListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String messageToSend = viewDataScience.getTextField().getText();

            if (messageToSend.length() != 0)
                publisher.publishDataScienceMessage(messageToSend);

            viewDataScience.getTextField().setText("");
        }
    }

    public class SendElectricGuitarsMessageListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String messageToSend = viewElectricGuitars.getTextField().getText();

            if (messageToSend.length() != 0)
                publisher.publishElectricGuitarsMessage(messageToSend);

            viewElectricGuitars.getTextField().setText("");
        }
    }

    public class SendFootballMessageListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String messageToSend = viewFootball.getTextField().getText();

            if (messageToSend.length() != 0)
                publisher.publishFootballMessage(messageToSend);

            viewFootball.getTextField().setText("");
        }
    }

    public void showFrames() {
        viewDataScience.getFrame().setVisible(true);
        viewElectricGuitars.getFrame().setVisible(true);
        viewFootball.getFrame().setVisible(true);
    }
}
