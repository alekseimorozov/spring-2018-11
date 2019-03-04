$(function() {
    getTask($("#taskId").val());

    function getTask(param) {
        $.get("/api/one?taskId=" + param, function (task) {
            fillTaskForm(task);
        });
    }

    $("#taskForm").submit(function(event) {
        event.preventDefault();
        $.post(
            "/api/save",
            $(this).serialize(),
            function (data) {
                fillTaskForm(data);
            });
    });

    function fillTaskForm(task) {
        $("#taskId").val(task.id);
        $("#title").val(task.title);
        $("#descr").val(task.description);
        $("#status").val(task.status);
        $("#progress").val(task.progress);
    }
});