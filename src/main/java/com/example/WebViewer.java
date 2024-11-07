package com.example;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebViewer extends JFrame {
    private JTextField urlField;
    private JButton runButton;
    private JEditorPane editorPane;
    private JList<String> linkList;
    private DefaultListModel<String> listModel;

    public WebViewer() {
        setTitle("Web Viewer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        urlField = new JTextField();
        runButton = new JButton("Run");
        editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");
        listModel = new DefaultListModel<>();
        linkList = new JList<>(listModel);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(urlField, BorderLayout.CENTER);
        topPanel.add(runButton, BorderLayout.EAST);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(editorPane), new JScrollPane(linkList));
        splitPane.setDividerLocation(500);

        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);

        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadPage(urlField.getText());
            }
        });

        editorPane.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    loadPage(e.getURL().toString());
                }
            }
        });
    }

    private void loadPage(String url) {
        try {
            editorPane.setPage(url);
            listModel.clear();
            List<String> links = extractLinks(editorPane.getText());
            for (String link : links) {
                listModel.addElement(link);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to load page: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<String> extractLinks(String html) {
        List<String> links = new ArrayList<>();
        Pattern pattern = Pattern.compile("href=\"(.*?)\"", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            links.add(matcher.group(1));
        }
        return links;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WebViewer().setVisible(true);
            }
        });
    }
}