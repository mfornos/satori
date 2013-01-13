module.exports = function (grunt) {
	
	grunt.loadNpmTasks('grunt-css');
	
    grunt.initConfig({
        concat : {
            eventsdist : {
                src : ['src/main/webapp/js/libs/jquery.min.js', 'src/main/webapp/js/libs/jquery.controls.js', 
                       'src/main/webapp/js/libs/jquery.form.js','src/main/webapp/js/libs/bootstrap.min.js',
                       'src/main/webapp/js/libs/jquery-ui-1.8.22.custom.min.js', 'src/main/webapp/js/libs/jquery-ui.datetime.js',
                       'src/main/webapp/js/libs/jquery-ui.map.min.js',
                       'src/main/webapp/js/libs/bootbox.min.js', 'src/main/webapp/js/libs/jquery.dialog2.js', 
                       'src/main/webapp/js/libs/jquery.charlimit.js', 'src/main/webapp/js/events/events-app.js'],
                dest : 'src/main/webapp/js/events-libs.js',
                separator : ';'
            },
            todosdist : {
                src : ['src/main/webapp/js/libs/json2.js', 'src/main/webapp/js/libs/jquery.min.js',            
                       'src/main/webapp/js/libs/jquery.atmosphere.js', 'src/main/webapp/js/libs/underscore.js', 
                       'src/main/webapp/js/libs/backbone.js', 'src/main/webapp/js/libs/backbone.modal.js'],
                dest : 'src/main/webapp/js/todos-libs.js',
                separator : ';'
            },
            metrodist : {
                src : ['src/main/webapp/js/libs/jquery.min.js', 'src/main/webapp/js/libs/jquery.fancybox.pack.js', 
                       'src/main/webapp/js/libs/jquery.scrollbar.min.js', 'src/main/webapp/js/libs/jquery.mousewheel-3.0.6.pack.js', 
                       'src/main/webapp/js/libs/jquery.pagination.js'],
                dest : 'src/main/webapp/js/metro-libs.js',
                separator : ';'
           },
           eventscss: {
                src: ['src/main/webapp/styles/ext/*.css','src/main/webapp/styles/ext/jquery-dialog2/*.css','src/main/webapp/styles/ext/ui-bootstrap/*.css', 'src/main/webapp/styles/eapp/eapp.css'],
                dest: 'src/main/webapp/styles/eapp/all.css'
           },
           metrocss: {
               src: ['src/main/webapp/styles/todos/todos.css','src/main/webapp/styles/ext/fancy/jquery.fancybox.css','src/main/webapp/styles/metro/*.css'],
               dest: 'src/main/webapp/styles/metro/all.css'
          }
        },
        min : {
            eventsdist : {
                src : ['src/main/webapp/js/events-libs.js'],
                dest : 'src/main/webapp/js/events-libs.min.js'
            },
            todosdist : {
                src : ['src/main/webapp/js/todos-libs.js'],
                dest : 'src/main/webapp/js/todos-libs.min.js'
            },
            metrodist : {
                src : ['src/main/webapp/js/metro-libs.js'],
                dest : 'src/main/webapp/js/metro-libs.min.js'
            }
        }
    });

    grunt.registerTask('default', 'concat min');
    
};
