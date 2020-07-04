<template>
    <div class="page-header">
        <h1>{{ title }}
            <small>
                <i class="ace-icon fa fa-angle-double-right">
                </i>
                <slot>Page</slot>
            </small>
        </h1>
    </div>
</template>

<script>
    export default {
        name: "pageHeader",
        props: ['title']
    }
</script>
