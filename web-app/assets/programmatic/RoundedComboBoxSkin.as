package assets.programmatic
{
	import mx.skins.Border;
	
	/**
	 * Esta clase reemplaza al HaloBorder.as de Flex. 
	 * Esto me permite, en este caso puntual, administrar el color de fondo, 
	 * de borde, aplicar una sombra (con/sin blur) y los bordes redondeados.
	 * 
	 * Aplicable a un ComboBox :
	 * skin: ClassReference('package.className') 
	 * over-skin: ClassReference('package.className')
	 * down-skin: ClassReference('package.className')
	 * icon-color: (default) 0x111111
	 */	
	
	public class RoundedComboBoxSkin extends Border
	{
		public function RoundedComboBoxSkin()
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
	                0-borderThickness, 0-borderThickness, unscaledWidth+borderThickness*2, unscaledHeight+borderThickness*2, 
	                {tl: cornerRadius, tr: cornerRadius, bl: cornerRadius, br: cornerRadius}, 
	                borderColor, backgroundAlpha
	            );
			}
			
			// Relleno
            drawRoundRect
            (
                0, 0, unscaledWidth, unscaledHeight, 
                {tl: cornerRadius, tr: cornerRadius, bl: cornerRadius, br: cornerRadius}, 
                backgroundColor, backgroundAlpha
            );
            
            
            switch (name)
			{
				case "overSkin":
				{
					graphics.clear();
					drawRoundRect
		            (
		                0, 0, unscaledWidth, unscaledHeight, 
		                {tl: cornerRadius, tr: cornerRadius, bl: cornerRadius, br: cornerRadius}, 
		                backgroundColor, 0.8
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
 
 			// Es combobox con el iconColor definido
 			//7x5
			if(!isNaN(iconColor) && iconColor != 0)
			{
				graphics.beginFill(iconColor);
				graphics.moveTo(unscaledWidth-18, 9);
				graphics.lineTo(unscaledWidth-11, 9);
				graphics.lineTo(unscaledWidth-14.5, 14);
				graphics.lineTo(unscaledWidth-18, 9);
				graphics.endFill();
			}
			
		}
		
		
	}
}