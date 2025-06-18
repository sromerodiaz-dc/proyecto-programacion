package com.editor.model.event;

import com.editor.model.record.EventData;
import com.game.controller.eventData.EventType;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class EventDialog extends JDialog {
    private EventData eventData;
    private boolean confirmed = false;

    public EventDialog(Frame parent, EventData initialData) {
        super(parent, "Configurar Evento", true);
        this.eventData = initialData;
        setupUI();
        if (initialData.texturePath() != null && !initialData.texturePath().isEmpty()) {
            try {
                BufferedImage img = ImageIO.read(new File(initialData.texturePath()));
                JLabel preview = new JLabel(new ImageIcon(img.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
                JOptionPane.showMessageDialog(this, preview, "Vista previa", JOptionPane.PLAIN_MESSAGE);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private void setupUI() {
        JPanel mainPanel = new JPanel(new GridLayout(0, 2, 5, 5));

        // Tipo de evento
        mainPanel.add(new JLabel("Tipo de Evento:"));
        JComboBox<EventType> typeCombo = new JComboBox<>(EventType.values());
        typeCombo.setSelectedItem(eventData.type());
        mainPanel.add(typeCombo);

        // Valor (daño/curación)
        mainPanel.add(new JLabel("Valor:"));
        JSpinner valueSpinner = new JSpinner(new SpinnerNumberModel(eventData.value(), -100, 100, 1));
        mainPanel.add(valueSpinner);

        // Cooldown
        mainPanel.add(new JLabel("Cooldown (seg):"));
        JSpinner cooldownSpinner = new JSpinner(new SpinnerNumberModel(eventData.cooldown(), 0, 300, 5));
        mainPanel.add(cooldownSpinner);

        // Mensaje
        mainPanel.add(new JLabel("Mensaje:"));
        JTextArea messageArea = new JTextArea(eventData.message(), 3, 20);
        mainPanel.add(new JScrollPane(messageArea));

        // Textura
        mainPanel.add(new JLabel("Textura:"));
        JPanel texturePanel = new JPanel(new BorderLayout());
        JTextField texturePath = new JTextField(eventData.texturePath());
        JButton browseButton = new JButton("Examinar...");
        texturePanel.add(texturePath, BorderLayout.CENTER);
        texturePanel.add(browseButton, BorderLayout.EAST);
        mainPanel.add(texturePanel);

        // Transformaciones
        mainPanel.add(new JLabel("Rotación (grados):"));
        JSpinner rotationSpinner = new JSpinner(new SpinnerNumberModel(eventData.rotation(), 0, 360, 15));
        mainPanel.add(rotationSpinner);

        mainPanel.add(new JLabel("Escala:"));
        JSpinner scaleSpinner = new JSpinner(new SpinnerNumberModel(eventData.scale(), 0.1, 3.0, 0.1));
        mainPanel.add(scaleSpinner);

        // Botones
        browseButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("src/main/resources/graphic/objects"));
            chooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "png", "jpg", "gif"));

            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                texturePath.setText(file.getPath());

                // Mostrar previsualización
                try {
                    BufferedImage img = ImageIO.read(file);
                    JLabel preview = new JLabel(new ImageIcon(img.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
                    JOptionPane.showMessageDialog(EventDialog.this, preview, "Vista previa", JOptionPane.PLAIN_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(EventDialog.this, "Error al cargar la imagen", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            confirmed = true;
            eventData = new EventData(
                    eventData.row(),
                    eventData.col(),
                    eventData.width(),
                    eventData.height(),
                    (EventType) typeCombo.getSelectedItem(),
                    messageArea.getText(),
                    (Integer) valueSpinner.getValue(),
                    (Integer) cooldownSpinner.getValue(),
                    texturePath.getText(),
                    (Double) rotationSpinner.getValue(),
                    (Double) scaleSpinner.getValue()
            );
            dispose();
        });

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(getParent());
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public EventData getEventData() {
        return eventData;
    }
}