package spacetraveler;

import java.io.IOException;
import java.util.Vector;
import java.awt.geom.*;

import org.jsfml.graphics.*;
import org.jsfml.system.*;
import org.jsfml.window.*;
import org.jsfml.window.event.Event.*;

import javafx.scene.shape.Circle;


public class Game {
	
	public static boolean hallo = false;
	
	public static float absVec(Vector2f v)
	{
		return (float)Math.abs(v.x*v.x+v.y*v.y);
	}
	
	public static float skalar(Vector2f a, Vector2f b)
	{
		return a.x*b.x+a.y*b.y;
	}
	
	public static Vector2f Schnittpunkt(SpaceObject A, SpaceObject B)
	{	
		float t = 0;	//Gleitpunkt
		t = (A.getCenter().x - B.getCenter().x)/(B.model.getVelocity().x-A.model.getVelocity().x);	//LGS
		return Vector2f.add(A.getCenter(), Vector2f.mul(A.model.getVelocity(), t));	//Einsetzen in die Formel (Inzidenzkriterium)
	}
	
	public static void schneiden(Vector<SpaceObject> spaceObjects)
	{
		if(spaceObjects.size() >= 2)
		{
			for(int i = 0; i <= spaceObjects.size(); i++)
			{
				for(int j = i+1; j < spaceObjects.size(); j++)
				{
					SpaceObject A = spaceObjects.elementAt(i);
					SpaceObject B = spaceObjects.elementAt(j);
					if(A.getCircle().intersects(B.getCircle().getBoundsInParent()) && A.getLine().intersectsLine(B.getLine()))
					{
						System.out.println(1);
						Vector2f P1 = A.getCenter();	//Zentrum 1
						Vector2f P2 = B.getCenter();	//Zentrum 2
						Vector2f P = Schnittpunkt(A, B);
						Vector2f F1 = Vector2f.add(P1, A.model.getVelocity());
						Vector2f F2 = Vector2f.add(P2, B.model.getVelocity());
						FloatRect R1 = new FloatRect(P1, A.model.getVelocity());
						FloatRect R2 = new FloatRect(P2, B.model.getVelocity());
						
						Vector2f v = Vector2f.sub(P, P1);
						Vector2f verh1 = Vector2f.add(v,Vector2f.mul(Vector2f.div(v,absVec(v)),(float)(A.getSprite().getLocalBounds().width*(Math.sqrt(2)/2))));
						Vector2f verh2 = Vector2f.sub(v,Vector2f.mul(Vector2f.div(v,absVec(v)),(float)(A.getSprite().getLocalBounds().width*(Math.sqrt(2)/2))));
						Vector2f verh3 = Vector2f.sub(P, P2);
						

						if(((absVec(verh1)/absVec(A.model.getVelocity())/2)
								<= absVec(verh3)/absVec(B.model.getVelocity())
								&& absVec(verh3)/absVec(B.model.getVelocity())
								<= absVec(verh2)/absVec(A.model.getVelocity())+10)){
						if((R1.contains(P) && R2.contains(P)) || absVec(Vector2f.sub(P1, P2)) <= (A.getSprite().getLocalBounds().width/2+B.getSprite().getLocalBounds().width+2))
						{
							hallo = true;
							Vector2f v1a = Vector2f.sub(P, P1);
							Vector2f v1b = Vector2f.sub(F2, P);
							Vector2f v2a = Vector2f.sub(P, P2);
							Vector2f v2b = Vector2f.sub(F1, P);
							
							Vector2f v1 = Vector2f.add(v1a, v1b);
							Vector2f v2 = Vector2f.add(v2a, v2b);
							
							A.getSprite().move(v1);
							B.getSprite().move(v2);
							
							Vector2f e_1 = new Vector2f(A.model.getEnergy().x*0.5f, A.model.getEnergy().y*0.5f);
							Vector2f v_1 = new Vector2f(A.model.getVelocity().x * 0.5f, A.model.getVelocity().y * 0.5f);
							
							Vector2f e_2 = new Vector2f(B.model.getEnergy().x*0.5f, B.model.getEnergy().y*0.5f);
							Vector2f v_2 = new Vector2f(B.model.getVelocity().x * 0.5f, B.model.getVelocity().y * 0.5f);
							
							A.model.Kollision(e_2,v_2);
							B.model.Kollision(e_1, v_1);
							System.out.println("geht");

							
							
		
							break;
						}
						}
					}					
				}
				
				
			}
		}
	}
	
	
	



	public static void main(String args[]) throws InterruptedException, IOException{
		Vector<SpaceObject> spaceObjects = new Vector<>();
		Vector<Gravity> gravityFields = new Vector<>();
		
		RenderWindow hauptfenster = new RenderWindow(new VideoMode(1000, 600), "SpaceTraveler");
		hauptfenster.clear();

		//Get the window's default view
		ConstView defaultView = hauptfenster.getView();
		
		Line2D.Float Linie = new Line2D.Float(5,0,0,5);
		Line2D.Float Linie2 = new Line2D.Float(2,2,10,10);
		
		int x = 100;
		
		Linie.intersectsLine(Linie2);
		
		//Create a new view by copying6
		//gravityFields.addElement(new Gravity(new Vector2f(300,300), 500));
		gravityFields.addElement(new Gravity(new Vector2f(500,300),500));
		//gravityFields.addElement(new Gravity(new Vector2f(800,600), 500));
		
		
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
        			spaceObjects.lastElement().getSprite().setColor(new Color(x,150+x/2,200));
        			x = (x + 100) % 500;
        		}
        		
			}
			

			hauptfenster.clear();
			
			
			
			
			// Berechnungen
			for(SpaceObject s : spaceObjects){
				Vector2f gesamtEnergie = new Vector2f(0, 0);
				
				if(s.model.isGravityOn()){
					for(Gravity g : gravityFields){
						{gesamtEnergie = Vector2f.add(gesamtEnergie, g.model.getEnergy(s));}
						
					}
				
					s.model.addEnergy(gesamtEnergie);
				}
				schneiden(spaceObjects);
				if(hallo == true){
					hallo = false;
				}
				else{s.move();}				
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
