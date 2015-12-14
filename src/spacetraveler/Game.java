package spacetraveler;

import java.io.IOException;
import java.util.Vector;

import org.jsfml.graphics.*;
import org.jsfml.system.*;
import org.jsfml.window.*;
import org.jsfml.window.event.Event.*;


public class Game {
	



	public static void main(String args[]) throws InterruptedException, IOException{
		Vector<SpaceObject> spaceObjects = new Vector<>();
		Vector<Gravity> gravityFields = new Vector<>();
		
		RenderWindow hauptfenster = new RenderWindow(new VideoMode(1200, 800), "SpaceTraveler");
		hauptfenster.clear();
		
		hauptfenster.setPosition(new Vector2i(-10,0));

		//Get the window's default view
		ConstView defaultView = hauptfenster.getView();

		//Create a new view by copying the window's default view
		//View view = new View(defaultView.getCenter(), defaultView.getSize());
		
		//hauptfenster.setView(view);
		
		spaceObjects.add(new SpaceObject("rsc/planet.png", 5.0f, new Vector2f(50, 0), new Vector2f(100, 100), true));
		
		
		gravityFields.addElement(new Gravity(new Vector2f(300,300), 10));
		//gravityFields.addElement(new Gravity(new Vector2f(500,300), 5));
		//gravityFields.addElement(new Gravity(new Vector2f(1200,400), 10));
		
		
		Vector2f Position1 = new Vector2f(0,0);				

		while(hauptfenster.isOpen()){
			// Events verarbeiten
			for(org.jsfml.window.event.Event ev : hauptfenster.pollEvents()){
        		if(ev.type == Type.CLOSED){
        			hauptfenster.close();
        		}
        		
        		if(ev.type == Type.MOUSE_BUTTON_PRESSED){
        			Position1 = new Vector2f(Mouse.getPosition(hauptfenster).x, Mouse.getPosition(hauptfenster).y);
        		}
        		
        		if(ev.type == Type.MOUSE_BUTTON_RELEASED){
        			Vector2f Position2 = new Vector2f(Mouse.getPosition(hauptfenster).x, Mouse.getPosition(hauptfenster).y);
        			
        			spaceObjects.addElement(new SpaceObject("rsc/planet.png",5.0f,Vector2f.sub(Position2, Position1),new Vector2f(Mouse.getPosition(hauptfenster).x, Mouse.getPosition(hauptfenster).y), true));

        		}
        		
			}
			

			hauptfenster.clear();
			
			//gravityFields.elementAt(0).model.m = Uhr.getElapsedTime().asSeconds() % 10;
			//gravityFields.elementAt(1).model.m = Uhr.getElapsedTime().asSeconds() % 20;
			
			
			
			// Berechnungen
			for(SpaceObject s : spaceObjects){
				Vector2f gesamtEnergie = new Vector2f(0, 0);
				
				if(s.model.isGravityOn()){
					for(Gravity g : gravityFields){
						gesamtEnergie = Vector2f.add(gesamtEnergie, g.model.getEnergy(s));
					}
				
					s.model.addEnergy(gesamtEnergie);
				}
				
				s.move();
				
				//view.setCenter(s.getSprite().getPosition().x,s.getSprite().getPosition().y);
				//hauptfenster.setView(view);
			}
			
				
			
			// Rendering
			// Alle Gravitys zeichnen
			for(Gravity g : gravityFields){
				hauptfenster.draw(g.getSprite());
			}
		
			// Alle SpaceObjects zeichnen!
			for(SpaceObject s : spaceObjects){	
				hauptfenster.draw(s.getSprite());
			}
			

			
			hauptfenster.display();
			Thread.sleep(1000/25);
		}
	}
	
}
