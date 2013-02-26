(function() {
    this.BioDrawing = (function() {
        function BioDrawing(empty) {
            this.empty = empty;
        }

        BioDrawing.prototype.paperWidth = 100;
        BioDrawing.prototype.drawingWidth = 0;
        BioDrawing.prototype.yPos = 0;
        BioDrawing.prototype.padding = 60;
        BioDrawing.prototype.pixelsPerBase = 10;
        BioDrawing.prototype.paper = '';
        BioDrawing.prototype.start = function(paperWidth, containerId) {
            this.paperWidth = paperWidth;
            this.drawingWidth = this.paperWidth - (this.padding * 2);
            return this.paper = Raphael(containerId, this.paperWidth, 2000);
        };
        var push_right = 40;
        BioDrawing.prototype.drawScoreScale = function(length) {
            var base, interval, xPos;
            this.pixelsPerBase = this.drawingWidth / length;
            var height = 9
            interval = length/5;
            /*
            //blast and functional
            this.drawScoreId(0,interval*5,12,'BLAST and functional annotation bitscores'); 
            this.drawScoreBar(0,interval,height,'red','','>=200');           
            this.drawScoreBar(interval,interval*2,height,'magenta','','80-200');
            this.drawScoreBar(interval*2,interval*3,height,'lime','','50-80');
            this.drawScoreBar(interval*3,interval*4,height,'blue','','40-50');
            this.drawScoreBar(interval*4,interval*5,height,'black','','<40');
            //interpro
            this.drawScoreId(interval*6,length,12,'InterProScan e-value'); 
            this.drawScoreBar(interval*6,interval*7,height,'red','','<= 1e-100');           
            this.drawScoreBar(interval*7,interval*8,height,'magenta','','1e-50 - 1e-100');
            this.drawScoreBar(interval*8,interval*9,height,'lime','','1e-20 - 1e-50');
            this.drawScoreBar(interval*9,interval*10,height,'blue','','1e-5 - 1e-20');
            this.drawScoreBar(interval*10,interval*11,height,'black','','1 - 1e-5');
        	*/
        	this.drawScoreBar(0,interval,height,'red','','<= 1e-100');           
            this.drawScoreBar(interval,interval*2,height,'magenta','','1e-50 - 1e-100');
            this.drawScoreBar(interval*2,interval*3,height,'lime','','1e-20 - 1e-50');
            this.drawScoreBar(interval*3,interval*4,height,'blue','','1e-5 - 1e-20');
            this.drawScoreBar(interval*4,interval*5,height,'black','','1 - 1e-5');
        };
    	BioDrawing.prototype.drawBlastScale = function(length) {
            var base, interval, xPos;
            this.pixelsPerBase = this.drawingWidth / length;
            var height = 9
            interval = length/5;
            //blast and functional
            this.drawScoreId(0,interval*5,12,'BLAST bit scores'); 
            this.drawScoreBar(0,interval,height,'red','','>=200');           
            this.drawScoreBar(interval,interval*2,height,'magenta','','80-200');
            this.drawScoreBar(interval*2,interval*3,height,'lime','','50-80');
            this.drawScoreBar(interval*3,interval*4,height,'blue','','40-50');
            this.drawScoreBar(interval*4,interval*5,height,'black','','<40');
        };
        
        BioDrawing.prototype.drawExon = function(length,exon_length,marker,number) {
            var base, interval, xPos;
            this.pixelsPerBase = this.drawingWidth / length;
            var height = 9
            this.drawBar(marker,marker+exon_length-0.5,height,'grey','Exon '+number,number);           
        };
        
        BioDrawing.prototype.drawScale = function(length) {
            var base, interval, xPos;
            this.pixelsPerBase = this.drawingWidth / length;
            //interval = 100
            if (length < 100){
            	interval = 10
            }else{
            	interval = length/20;
            }
            interval = Math.round(interval/10)*10
            this.paper.rect(this.padding+push_right, this.yPos, this.drawingWidth, 2).attr({
                fill: 'black',
                'stroke-width': '0'
            });
            for (base = 0; 0 <= length ? base <= length : base >= length; base += interval) {
                xPos = base * this.pixelsPerBase;
                this.paper.rect(xPos + this.padding+push_right, this.yPos, 1, 10).attr({
                    fill: 'black',
                    'stroke-width': '0'
                });
                this.paper.text(xPos + this.padding+push_right, this.yPos + 12, base);
            }
            return this.yPos = this.yPos + 20;
        };
        BioDrawing.prototype.drawLine = function(length) {
            var base, interval, xPos;
            this.pixelsPerBase = this.drawingWidth / length;
            this.paper.rect(this.padding+push_right, this.yPos, this.drawingWidth, 2).attr({
                fill: 'grey',
                'stroke-width': '0'
            });
            return this.yPos = this.yPos + 20;
        };
        BioDrawing.prototype.drawChart = function(data, height) {
            var x, xValues;
            xValues = (function() {
                var _ref, _results;
                _results = [];
                for (x = 0, _ref = data.length; 0 <= _ref ? x <= _ref : x >= _ref; 0 <= _ref ? x++ : x--) {
                    _results.push(x * this.pixelsPerBase);
                }
                return _results;
            }).call(this);
            this.paper.g.linechart(this.padding, this.yPos, this.drawingWidth, height, xValues, data, {
                axis: '0 1 0 1'
            });
            return this.yPos = this.yPos + height + 20;
        };
        
     	BioDrawing.prototype.drawScoreBar = function(start, stop, height, colour, description, text) {
            var bar, width;
            width = (stop * this.pixelsPerBase) - (start * this.pixelsPerBase);
            bar = this.paper.rect((start * this.pixelsPerBase) + this.padding +push_right, this.yPos, width, height).attr({
                fill: colour,
                'stroke-width': '0',
                'title': description
            });
            var textColour = 'black';
            var textYPosition = this.yPos-10;
            var textPosition = ((start * this.pixelsPerBase + this.padding +push_right) + (stop * this.pixelsPerBase + this.padding +push_right)) / 2;
            var title = this.paper.text(textPosition, textYPosition, text).attr({
                'font-size': (height * 1),
                'fill' : textColour
            })
            return bar;
        };
        
        BioDrawing.prototype.drawBar = function(start, stop, height, colour, description, text) {
            var bar, width;
            width = (stop * this.pixelsPerBase) - (start * this.pixelsPerBase);
            bar = this.paper.rect((start * this.pixelsPerBase) + this.padding +push_right, this.yPos, width, height).attr({
                fill: colour,
                'stroke-width': '0',
                'title': description,
                'cursor': 'pointer'
            });
            var textColour = 'black';
            var textYPosition = this.yPos + (height / 2);
            var textPosition = ((start * this.pixelsPerBase + this.padding +push_right) + (stop * this.pixelsPerBase + this.padding +push_right)) / 2;
            var title = this.paper.text(textPosition, textYPosition, text).attr({
                'font-size': (height * 1),
                'fill' : textColour
            })
            return bar;
        };
        
        BioDrawing.prototype.drawScoreId = function(start, stop, height, text) {
            var bar, width;
            //width = (stop * this.pixelsPerBase) - (start * this.pixelsPerBase);
			var textPosition = ((start * this.pixelsPerBase + this.padding +push_right) + (stop * this.pixelsPerBase + this.padding +push_right)) / 2;            
			var textYPosition = this.yPos - 25;
            var textColour = 'black';
            var title = this.paper.text(textPosition, textYPosition, text).attr({
                'font-size': (height * 1),
                'fill' : textColour
            })
            return bar;
        };
        
        BioDrawing.prototype.drawId = function(start, stop, height, colour, description, text) {
            var bar, width;
            //width = (stop * this.pixelsPerBase) - (start * this.pixelsPerBase);
            var textPosition = 0;
            var textYPosition = this.yPos + (height / 2);
            var textColour = 'black';
            var title = this.paper.text(textPosition, textYPosition, text.substr(0,15)).attr({
                'font-size': (height * 0.9),
                'fill' : textColour,
                'text-anchor': 'start'
            })
            return bar;
        };
        
        BioDrawing.prototype.drawTitle = function(text) {
            var title;
            title = this.paper.text(this.drawingWidth / 2, this.yPos, text).attr({
                'font-size': 14
            });
            this.yPos = this.yPos + 20;
            return title;
        };
        BioDrawing.prototype.drawColouredTitle = function(text, colour) {
            var title;
            title = this.paper.text(0, this.yPos, text).attr({
                'font-size': 14,
                'fill' : colour,
                'text-anchor': 'start'
            });
            this.yPos = this.yPos - 10;
            return title;
        };
        BioDrawing.prototype.drawSpacer = function(pixels) {
            return this.yPos = this.yPos + pixels;
        };
        BioDrawing.prototype.getBLASTColour = function(score,annoType) {
        	var hitColour;
        	/*
        	if (annoType === 'ipr'){
        		if (score < 1){hitColour = 'black';}
        		if (score < 1e-5){hitColour = 'blue';}
        		if (score < 1e-20){hitColour = 'lime';}
        		if (score < 1e-50){hitColour = 'magenta';}
        		if (score < 1e-100){hitColour = 'red';}				
        	}else{
				if (score < 1000000){hitColour = 'red';}
				if (score < 200){hitColour = 'magenta';}
				if (score < 80){hitColour = 'lime';}
				if (score < 50){hitColour = 'blue';}
				if (score < 40){hitColour = 'black';}     
			} 
			*/  
			if (score < 1){hitColour = 'black';}
        	if (score < 1e-5){hitColour = 'blue';}
        	if (score < 1e-20){hitColour = 'lime';}
        	if (score < 1e-50){hitColour = 'magenta';}
        	if (score < 1e-100){hitColour = 'red';}	
            return hitColour;
        };
        BioDrawing.prototype.end = function() {
            return this.paper.setSize(this.paperWidth, this.yPos);
        };
        return BioDrawing;
    })();
}).call(this);
