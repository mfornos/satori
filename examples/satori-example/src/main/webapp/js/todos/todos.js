// An example Backbone application contributed by
// [Jérôme Gravel-Niquet](http://jgn.me/).
// Adapted to Satori Atmosphere backend.
// Load the application once the DOM is ready, using `jQuery.ready`:
$(function () {

  // Todo Model
  // ----------

  // Our basic **Todo** model has `title`, `order`, and `done` attributes.
  var Todo = Backbone.Model.extend({

    urlRoot: ctx,

    idAttribute: 'order',

    // Default attributes for the todo item.
    defaults: function () {
      return {
        title: "empty todo...",
        order: Todos.nextOrder(),
        done: false
      };
    },

    // Ensure that each todo created has `title`.
    initialize: function () {
      if (!this.get("title")) {
        this.set({
          "title": this.defaults().title
        });
      }
    },

    // Toggle the `done` state of this todo item.
    toggle: function () {
      this.save({
        done: !this.get("done")
      });
    },

    // Remove this Todo and delete its view.
    clear: function () {
      this.destroy();
    }

  });

  // Todo Collection
  // ---------------

  // The collection of todos is backed by a remote server.
  var TodoList = Backbone.Collection.extend({

    // Reference to this collection's model.
    model: Todo,

    url: ctx + 'all',

    // Filter down the list of all todo items that are finished.
    done: function () {
      return this.filter(function (todo) {
        return todo.get('done');
      });
    },

    // Filter down the list to only todo items that are still not finished.
    remaining: function () {
      return this.without.apply(this, this.done());
    },

    // We keep the Todos in sequential order, despite being saved by unordered
    // GUID in the database. This generates the next order number for new items.
    nextOrder: function () {
      if (!this.length) return 1;
      return this.last().get('order') + 1;
    },

    // Todos are sorted by their original insertion order.
    comparator: function (todo) {
      return todo.get('order');
    }

  });

  // Create our global collection of **Todos**.
  var Todos = new TodoList;

  // Todo Item View
  // --------------

  // The DOM element for a todo item...
  var TodoView = Backbone.View.extend({

    //... is a list tag.
    tagName: "li",

    // Cache the template function for a single item.
    template: _.template($('#item-template').html()),

    // The DOM events specific to an item.
    events: {
      "click .toggle": "toggleDone",
      "dblclick .view": "edit",
      "click a.destroy": "clear",
      "keypress .edit": "updateOnEnter",
      "blur .edit": "close"
    },

    // The TodoView listens for changes to its model, re-rendering. Since there's
    // a one-to-one correspondence between a **Todo** and a **TodoView** in this
    // app, we set a direct reference on the model for convenience.
    initialize: function () {
      this.model.on('change', this.render, this);
      this.model.on('destroy', this.remove, this);
    },

    // Re-render the titles of the todo item.
    render: function () {
      this.$el.html(this.template(this.model.toJSON()));
      this.$el.toggleClass('done', this.model.get('done'));
      this.input = this.$('.edit');
      return this;
    },

    // Toggle the `"done"` state of the model.
    toggleDone: function () {
      this.model.toggle();
    },

    // Switch this view into `"editing"` mode, displaying the input field.
    edit: function () {
      this.$el.addClass("editing");
      this.input.focus();
    },

    // Close the `"editing"` mode, saving changes to the todo.
    close: function () {
      var value = this.input.val();
      if (!value) this.clear();
      this.model.save({
        title: value
      });
      this.$el.removeClass("editing");
    },

    // If you hit `enter`, we're through editing the item.
    updateOnEnter: function (e) {
      if (e.keyCode == 13) this.close();
    },

    // Remove the item, destroy the model.
    clear: function () {
      this.model.clear();
    }

  });

  // Auth Model
  // ----------

  var Authentication = Backbone.Model.extend({
    url: ctx + "signin"
  });

  // Login View
  // ----------

  var LoginView = Backbone.ModalView.extend({

    template: _.template($('#login-template').html()),
    model: new Authentication(),
    initialize: function () {
      var that = this;

      _.bindAll(this, "render");

      $("#login-button").click(

      function (event) {
        // Create the modal view
        that.render().showModal({
          showCloseButton: false,
          y: 50
        });
      });
    },
    events: {
      "submit form": "login"
    },
    login: function (e) {
      errors = $(this.el).find("#errors");
      this.model.save({
        username: $(this.el).find("#username").val(),
        password: $(this.el).find("#password").val(),
        rememberMe: $(this.el).find("#rememberMe").is(':checked'),
        loginAccepted: false
      }, {
        success: function (model, response) {
          if (model.get("loginAccepted")) {
            window.location.reload();
          } else {
            errors.hide().html('<div class="alert-error alert"><p><strong>Bad credentials</strong>. Please, try again.</p></div>').fadeIn();
          }
        },
        error: function (model, response) {
          errors.hide().html('<div class="alert-error alert"><p><strong>Ooops!</strong> Server error. Please, try again.</p></div>').fadeIn();
        }
      });
      e.preventDefault();
    },
    render: function () {
      $(this.el).html(this.template());
      return this;
    }

  });

  // The Application
  // ---------------

  // Our overall **AppView** is the top-level piece of UI.
  var AppView = Backbone.View.extend({

    // Instead of generating a new element, bind to the existing skeleton of
    // the App already present in the HTML.
    el: $("#todoapp"),

    // Our template for the line of statistics at the bottom of the app.
    statsTemplate: _.template($('#stats-template').html()),

    // Delegated events for creating new items, and clearing completed ones.
    events: {
      "keypress #new-todo": "createOnEnter",
      "click #clear-completed": "clearCompleted",
      "click #toggle-all": "toggleAllComplete"
    },

    loginView: new LoginView,

    // At initialization we bind to the relevant events on the `Todos`
    // collection, when items are added or changed. Kick things off by
    // loading any preexisting todos that might be saved in *localStorage*.
    initialize: function () {

      this.subscribe();

      this.input = this.$("#new-todo");
      this.allCheckbox = this.$("#toggle-all")[0];

      Todos.on('add', this.addOne, this);
      Todos.on('reset', this.addAll, this);
      Todos.on('all', this.render, this);

      this.footer = this.$('footer');
      this.main = $('#main');

      Todos.fetch();

    },

    subscribe: function () {

      $.atmosphere.subscribe({
        url: ctx + "suscribe/updates",
        contentType: "application/json",
        transport: 'websocket',
        fallbackTransport: 'long-polling',
        onMessage: function (response) {
          var message = response.responseBody;
          var nt = new Todo(JSON.parse(message));
          var ot = Todos.get(nt.id);
          if (ot) ot.set(nt);
          else Todos.create(nt);
        }
      });

      $.atmosphere.subscribe({
        url: ctx + "suscribe/deletes",
        contentType: "application/json",
        transport: 'websocket',
        fallbackTransport: 'long-polling',
        onMessage: function (response) {
          var message = response.responseBody;
          var t = Todos.get(parseInt(message));
          if (t) t.clear();
        }
      });

    },

    // Re-rendering the App just means refreshing the statistics -- the rest
    // of the app doesn't change.
    render: function () {
      var done = Todos.done().length;
      var remaining = Todos.remaining().length;

      if (Todos.length) {
        this.main.show();
        this.footer.show();
        this.footer.html(this.statsTemplate({
          done: done,
          remaining: remaining
        }));
      } else {
        this.main.hide();
        this.footer.hide();
      }

      this.allCheckbox.checked = !remaining;
    },

    // Add a single todo item to the list by creating a view for it, and
    // appending its element to the `<ul>`.
    addOne: function (todo) {
      var view = new TodoView({
        model: todo
      });
      this.$("#todo-list").append(view.render().el);
    },

    // Add all items in the **Todos** collection at once.
    addAll: function () {
      // Simple yet inefficient hack to avoid managing deltas at the backend
      this.$("#todo-list").empty();
      Todos.each(this.addOne);
    },

    // If you hit return in the main input field, create new **Todo** model,
    // persisting it to *localStorage*.
    createOnEnter: function (e) {
      if (e.keyCode != 13) return;
      if (!this.input.val()) return;

      Todos.create({
        title: this.input.val()
      });
      this.input.val('');
    },

    // Clear all done todo items, destroying their models.
    clearCompleted: function () {
      _.each(Todos.done(), function (todo) {
        todo.clear();
      });
      return false;
    },

    toggleAllComplete: function () {
      var done = this.allCheckbox.checked;
      Todos.each(function (todo) {
        todo.save({
          'done': done
        });
      });
    }

  });

  // Finally, we kick things off by creating the **App**.
  var App = new AppView;

});
