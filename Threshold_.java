import java.awt.AWTEvent;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.DialogListener;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

public class Threshold_ implements  PlugIn, DialogListener{
	
	private ImagePlus imagem;
	private ImageProcessor processador;
	private ImageProcessor imagemCopia;
	private ImagePlus cinza;
	private ImageProcessor processadorCinza;
	

	public void run(String arg) {	
		
		Interface_Grafica();	
		
	}
	
	public void Interface_Grafica() {
		
		GenericDialog interfaceGrafica = new GenericDialog("Threshold");
		
		this.imagem = IJ.getImage();
		this.processador = this.imagem.getProcessor();
		CriarImagemCinza();
		
		interfaceGrafica.addDialogListener(this);
		interfaceGrafica.addMessage("Plugin para aplicar a t�cnica de Threshold");
		interfaceGrafica.addSlider("Threshold", 0, 255, 0, 1);		
		interfaceGrafica.showDialog();
		
		if (interfaceGrafica.wasCanceled()) {
			IJ.showMessage("PlugIn cancelado!");
			resetarImagem();
		}
		else {
			if (interfaceGrafica.wasOKed()) {				
		        IJ.showMessage("Plugin encerrado com sucesso!");
			}
		}
	}

	
	private void resetarImagem() {
		
		this.cinza.setProcessor(this.processadorCinza);
		this.cinza.updateAndDraw();
		
	}
	

	private void CriarImagemCinza() {
		
		int mediaPixels;
		int pixel[] = new int[3];		
		cinza = IJ.createImage("Cinza", "8-bit", processador.getWidth(), processador.getHeight(), 1);
		this.processadorCinza = cinza.getProcessor();
		
		for (int i = 0; i < processador.getWidth(); i++) {
			for (int j = 0; j < processador.getHeight(); j++) {
				pixel = processador.getPixel(i, j, pixel);
				mediaPixels = (pixel[0] + pixel[1] + pixel[2]) / 3;
				processadorCinza.putPixel(i, j, mediaPixels);
			}
		}
		
		cinza.show();
	}


	
	@Override
	public boolean dialogItemChanged(GenericDialog interfaceGrafica, AWTEvent e) {
		
		if (interfaceGrafica.wasCanceled()) return false;
		int sliderThreshold = (int) interfaceGrafica.getNextNumber();
	
		aplicarThreshold(sliderThreshold);		
        
        IJ.log("Valor atual do slider: "  + sliderThreshold );
        
        return true;
    }

	private void aplicarThreshold(int sliderThreshold) {
		
		int pixel[] = {0};
		this.cinza.setProcessor(this.processadorCinza);
		this.imagemCopia = this.processadorCinza.duplicate();
		
		for(int i = 0; i < processadorCinza.getWidth(); i++){
			for(int j= 0; j < processadorCinza.getHeight(); j++){
				
				pixel =   processadorCinza.getPixel(i, j, pixel);
				
				if ( pixel[0] < sliderThreshold) {
					
					pixel[0] = 0;
                }
                if (pixel[0] > sliderThreshold) {
                	
                	pixel[0] = 255;
                }
                
                imagemCopia.putPixel( i, j, pixel[0]);
				
			}
		}
		
		this.cinza.setProcessor(imagemCopia);
		this.cinza.updateAndDraw();		
	}	
}