package assets.programmatic
{
	import mx.skins.Border;
	
	/**
	 * Esta clase reemplaza al HaloBorder.as de Flex. 
	 * Esto me permite, en este caso puntual, administrar el color de fondo, 
	 * de borde, aplicar una sombra (con/sin blur) y los bordes redondeados.
	 * 
	 * Aplicable a elementos de un form (borderSkin:ClassReference('package.className')) 
	 * y botones (skin:ClassReference('package.className')).
	 */	
	
	public class RoundedCornersSkin extends Border
	{
		public function RoundedCornersSkin()
		{
			super();
		}
		
		
		
		override protected function updateDisplayList(unscaledWidth:Number,
												unscaledHeight:Number):void
		{
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			
			var cornerRadius:Number = getStyle("cornerRadius");
            var backgroundColor:int = getStyle("backgroundColor");
            var backgroundAlpha:Number = getStyle("backgroundAlpha");
            var borderThickness:Number = getStyle("borderThickness");
			var borderStyle:String = getStyle("borderStyle");
			var borderColor:Number = getStyle("borderColor");
			
			var iconColor:Number = getStyle("iconColor");
			
			if(backgroundColor == 0) backgroundColor = borderColor;
			            
            graphics.clear();
            
            if(borderStyle == "solid")
			{
				//Borde
				drawRoundRect
	            (
	                borderThickness, borderThickness, unscaledWidth+borderThickness, unscaledHeight+borderThickness, 
	                {tl: cornerRadius, tr: cornerRadius, bl: cornerRadius, br: cornerRadius}, 
	                borderColor, backgroundAlpha-0.3
	            );
			}
			
			// Relleno
            drawRoundRect
            (
                0, 0, unscaledWidth, unscaledHeight, 
                {tl: cornerRadius, tr: cornerRadius, bl: cornerRadius, br: cornerRadius}, 
                backgroundColor, backgroundAlpha
            );
            
            // Para un Button
            switch (name)
			{
				case "overSkin":
				{
					graphics.clear();
					drawRoundRect
		            (
		                0, 0, unscaledWidth, unscaledHeight, 
		                {tl: cornerRadius, tr: cornerRadius, bl: cornerRadius, br: cornerRadius}, 
		                backgroundColor, 0.9
		            );
		            break;
				}
				case "disabledSkin":
				{
					graphics.clear();
					drawRoundRect
		            (
		                0, 0, unscaledWidth, unscaledHeight, 
		                {tl: cornerRadius, tr: cornerRadius, bl: cornerRadius, br: cornerRadius}, 
		                backgroundColor, 0.4
		            );
	            break;
				}
				case "downSkin":
			 	case "upSkin": 
				//graphics.clear();
				drawRoundRect
	            (
	                0, 0, unscaledWidth, unscaledHeight, 
	                {tl: cornerRadius, tr: cornerRadius, bl: cornerRadius, br: cornerRadius}, 
	                backgroundColor, backgroundAlpha
	            );
	            break;
	            
			}
 

			
		}
		
		
		
	}
}