package assets.programmatic
{
	import flash.display.GradientType;
	
	import mx.skins.Border;

	public class GradientButtonSkin extends Border
	{
		
		private var dropShadowEnabled:Boolean = true;
		
		public function GradientButtonSkin()
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
			
			var fillAlphas:Array = getStyle("fillAlphas");
			var fillColors:Array = getStyle("fillColors");
			var gradientType:String = getStyle("gradientType");
			var angle:Number = getStyle("angle");
			var focalPointRatio:Number = getStyle("focalPointRatio");
			
			dropShadowEnabled = getStyle("dropShadowEnabled");
			
			
			if(backgroundColor == 0) backgroundColor = borderColor;
			            
            graphics.clear();
            
            // VAlores default
			if (fillColors == null)
			//	fillColors = [0xEEEEEE, 0x999999];
			
			if (fillAlphas == null)
				fillAlphas = [1, 1];
			
			if (gradientType == "" || gradientType == null)
				gradientType = GradientType.LINEAR;
			
			if (isNaN(angle))
				angle = 90;
				
			if (isNaN(focalPointRatio))
				focalPointRatio = 0.5;
            
            
            if(borderStyle == "solid")
			{
				//Borde: Queda una sombra defasada
				drawRoundRect
	            (
	                borderThickness, borderThickness, unscaledWidth+borderThickness, unscaledHeight+borderThickness, 
	                {tl: cornerRadius, tr: cornerRadius, bl: cornerRadius, br: cornerRadius}, 
	                borderColor, backgroundAlpha-0.4
	            );
	           
			}
			if(borderStyle == "outset")
			{
				//Borde: Borde normal
				drawRoundRect
	            (
	                0-borderThickness, 0-borderThickness, unscaledWidth+borderThickness*2, unscaledHeight+borderThickness*2, 
	                {tl: cornerRadius, tr: cornerRadius, bl: cornerRadius, br: cornerRadius}, 
	                borderColor, backgroundAlpha-0.4
	            );
	           
			}
			/* 
			// Relleno
            drawRoundRect
            (
                0, 0, unscaledWidth, unscaledHeight, 
                {tl: cornerRadius, tr: cornerRadius, bl: cornerRadius, br: cornerRadius}, 
                backgroundColor, backgroundAlpha
            );  */
            
            // Para un Button
            switch (name)
			{
				case "overSkin":
				{
					//graphics.clear();
					drawRoundRect
		            (
		                0, 0, unscaledWidth, unscaledHeight, 
		                {tl: cornerRadius, tr: cornerRadius, bl: cornerRadius, br: cornerRadius}, 
		                fillColors, backgroundAlpha-0.3,
		                verticalGradientMatrix(0,0,unscaledWidth, unscaledHeight),
		                GradientType.LINEAR,
		                null
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
				
				drawRoundRect
	            (
	                0, 0, unscaledWidth, unscaledHeight, 
	                {tl: cornerRadius, tr: cornerRadius, bl: cornerRadius, br: cornerRadius}, 
	                fillColors, backgroundAlpha,
	                verticalGradientMatrix(0,0,unscaledWidth, unscaledHeight),
	                GradientType.LINEAR,
	                null
	            );
	          
	            break;
	            
			}
 

			
		}
		
	}
}